package com.elilouve.wingsclassic.flight;

import com.elilouve.wingsclassic.config.WingsConfig;
import com.elilouve.wingsclassic.item.WingsItem;
import com.elilouve.wingsclassic.util.CubicBezier;
import com.elilouve.wingsclassic.util.WingsMath;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

/** Logique de vol, portée du mod Wings 1.12.2 (MIT, Pau101). */
public final class FlightDefault implements Flight, INBTSerializable<CompoundTag> {
    private static final CubicBezier FLY_AMOUNT_CURVE = new CubicBezier(0.37F, 0.13F, 0.3F, 1.12F);
    private static final int INITIAL_TIME_FLYING = 0;
    private static final int MAX_TIME_FLYING = 20;
    private static final float PITCH_OFFSET = 30.0F;

    private final List<FlyingListener> flyingListeners = new ArrayList<>();
    private final List<SyncListener> syncListeners = new ArrayList<>();

    private int prevTimeFlying = INITIAL_TIME_FLYING;
    private int timeFlying = INITIAL_TIME_FLYING;
    private boolean isFlying;
    private boolean clientHooked;
    private boolean grantedMayFly;
    private int flightTime;
    private Vec3 lastPos;

    @Override
    public void setIsFlying(final boolean isFlying, final PlayerSet players) {
        if (this.isFlying != isFlying) {
            this.isFlying = isFlying;
            for (final FlyingListener listener : this.flyingListeners) {
                listener.onChange(isFlying);
            }
            this.sync(players);
        }
    }

    @Override
    public boolean isFlying() {
        return this.isFlying;
    }

    @Override
    public void setTimeFlying(final int timeFlying) {
        this.timeFlying = timeFlying;
    }

    @Override
    public int getTimeFlying() {
        return this.timeFlying;
    }

    @Override
    public float getFlyingAmount(final float delta) {
        return FLY_AMOUNT_CURVE.eval(Mth.lerp(delta, this.prevTimeFlying, this.timeFlying) / MAX_TIME_FLYING);
    }

    @Override
    public void registerFlyingListener(final FlyingListener listener) {
        this.flyingListeners.add(listener);
    }

    @Override
    public void registerSyncListener(final SyncListener listener) {
        this.syncListeners.add(listener);
    }

    @Override
    public boolean claimClientHook() {
        if (this.clientHooked) {
            return false;
        }
        this.clientHooked = true;
        return true;
    }

    @Override
    public boolean canFly(final Player player) {
        final ItemStack stack = WingsHelper.findWings(player);
        return stack.getItem() instanceof WingsItem wings && wings.isUsable(player, stack);
    }

    @Override
    public boolean canLand(final Player player, final ItemStack wings) {
        return wings.getItem() instanceof WingsItem item && item.isLandable(player, wings);
    }

    private void onWornUpdate(final Player player, final ItemStack stack, final WingsItem wings) {
        if (player.isLocalPlayer()) {
            if (this.isFlying) {
                final float sprintCap = WingsConfig.sprintMaxSpeed();
                final boolean sprinting = sprintCap > 0.0F && player.isSprinting();
                float speed = (float) Mth.clampedLerp(WingsConfig.minSpeed(), WingsConfig.maxSpeed(), player.zza);
                if (sprinting) {
                    // Accélération qui permet d'atteindre sprintCap malgré la friction de l'air (0.91 par tick)
                    speed = Math.max(speed, sprintCap * 0.099F);
                }
                final float elevationBoost = WingsMath.transform(Math.abs(player.getXRot()), 45.0F, 90.0F, 1.0F, 0.0F);
                final float pitch = -((player.getXRot() - PITCH_OFFSET * elevationBoost) * Mth.DEG_TO_RAD);
                final float yaw = -(player.getYRot() * Mth.DEG_TO_RAD) - Mth.PI;
                final float vxz = -Mth.cos(pitch);
                final float vy = Mth.sin(pitch);
                final float vz = Mth.cos(yaw);
                final float vx = Mth.sin(yaw);
                final Vec3 motion = player.getDeltaMovement();
                Vec3 next = new Vec3(
                    motion.x + vx * vxz * speed,
                    motion.y + vy * speed + WingsConfig.lift() * (player.getXRot() > 0.0F ? elevationBoost : 1.0D),
                    motion.z + vz * vxz * speed
                );
                if (sprinting && next.length() > sprintCap) {
                    next = next.scale(sprintCap / next.length());
                }
                player.setDeltaMovement(next);
                if (WingsConfig.debugSpeed()) {
                    player.displayClientMessage(net.minecraft.network.chat.Component.literal(String.format(
                        "Vitesse : %.1f blocs/s | sprint : %s | plafond sprint : %.2f | accélération : %.3f",
                        next.length() * 20.0D, sprinting ? "OUI" : "non", sprintCap, speed)), true);
                }
            }
        }
        if (WingsConfig.landingAssist() && this.canLand(player, stack)) {
            if (player.isLocalPlayer()) {
                final Vec3 motion = player.getDeltaMovement();
                if (motion.y < 0.0D) {
                    player.setDeltaMovement(motion.x, motion.y * WingsConfig.fallReduction(), motion.z);
                }
            }
            player.fallDistance = 0.0F;
        }
        if (!player.level().isClientSide) {
            if (wings.isUsable(player, stack)) {
                if (this.isFlying) {
                    final int damageRate = WingsConfig.damageInterval();
                    if (this.flightTime++ % damageRate == damageRate - 1) {
                        if (player.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                            stack.hurtAndBreak(1, serverLevel, player, item -> player.onEquippedItemBroken(item, EquipmentSlot.CHEST));
                        }
                    }
                } else {
                    this.flightTime = 0;
                }
            } else if (this.isFlying) {
                this.setIsFlying(false, PlayerSet.ofAll());
                this.flightTime = 0;
            }
        }
    }

    @Override
    public void tick(final Player player, final ItemStack wings) {
        if (!wings.isEmpty() && wings.getItem() instanceof WingsItem item) {
            this.onWornUpdate(player, wings, item);
            if (!player.level().isClientSide) {
                this.applyExertion(player, wings, item);
            }
        } else if (!player.level().isClientSide && this.isFlying) {
            this.setIsFlying(false, PlayerSet.ofAll());
        }
        if (player instanceof ServerPlayer) {
            this.updateMayFly(player);
        }
        this.lastPos = player.position();
        this.prevTimeFlying = this.timeFlying;
        if (this.isFlying) {
            if (this.timeFlying < MAX_TIME_FLYING) {
                this.timeFlying++;
            } else if (player.isLocalPlayer() && player.onGround()) {
                this.setIsFlying(false, PlayerSet.ofOthers());
            }
        } else if (this.timeFlying > INITIAL_TIME_FLYING) {
            this.timeFlying--;
        }
    }

    /** Fatigue due au déplacement (équivalent de l'ancien hook addFlown). */
    private void applyExertion(final Player player, final ItemStack stack, final WingsItem wings) {
        if (this.lastPos == null) {
            return;
        }
        final Vec3 direction = player.position().subtract(this.lastPos);
        if (this.isFlying) {
            wings.onFlight(player, direction);
        } else if (player.getDeltaMovement().y < -0.5D) {
            wings.onLanding(player);
        }
    }

    /** Le serveur kicke les joueurs qui "flottent" : on leur accorde mayfly tant qu'ils volent. */
    private void updateMayFly(final Player player) {
        final boolean exempt = player.isCreative() || player.isSpectator();
        if (this.isFlying) {
            if (!player.getAbilities().mayfly) {
                player.getAbilities().mayfly = true;
                this.grantedMayFly = true;
            }
        } else if (this.grantedMayFly) {
            this.grantedMayFly = false;
            if (!exempt) {
                player.getAbilities().mayfly = false;
            }
        }
    }

    @Override
    public void clone(final Flight other) {
        this.setIsFlying(other.isFlying());
        this.setTimeFlying(other.getTimeFlying());
    }

    @Override
    public void sync(final PlayerSet players) {
        for (final SyncListener listener : this.syncListeners) {
            listener.onSync(players);
        }
    }

    @Override
    public CompoundTag serializeNBT(final HolderLookup.Provider provider) {
        final CompoundTag tag = new CompoundTag();
        tag.putBoolean("isFlying", this.isFlying);
        tag.putInt("timeFlying", this.timeFlying);
        return tag;
    }

    @Override
    public void deserializeNBT(final HolderLookup.Provider provider, final CompoundTag tag) {
        this.setIsFlying(tag.getBoolean("isFlying"));
        this.setTimeFlying(tag.getInt("timeFlying"));
    }
}
