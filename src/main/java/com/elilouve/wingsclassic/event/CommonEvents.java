package com.elilouve.wingsclassic.event;

import com.elilouve.wingsclassic.WingsClassic;
import com.elilouve.wingsclassic.config.WingsConfig;
import com.elilouve.wingsclassic.flight.Flight;
import com.elilouve.wingsclassic.flight.Flights;
import com.elilouve.wingsclassic.flight.WingsHelper;
import com.elilouve.wingsclassic.net.ControlFlyingMessage;
import com.elilouve.wingsclassic.net.SyncFlightMessage;
import com.elilouve.wingsclassic.net.WingsNetwork;
import com.elilouve.wingsclassic.registry.WingsItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = WingsClassic.ID)
public final class CommonEvents {
    private CommonEvents() {}

    @SubscribeEvent
    public static void onAttachCapabilities(final AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof Player player)) {
            return;
        }
        final Flights.Provider provider = new Flights.Provider();
        final Flight flight = provider.flight();
        if (player instanceof ServerPlayer serverPlayer) {
            flight.registerFlyingListener(isFlying -> {
                if (isFlying) {
                    serverPlayer.stopRiding();
                }
            });
            final Flight.Notifier notifier = Flight.Notifier.of(
                () -> WingsNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncFlightMessage(serverPlayer.getId(), flight)),
                p -> WingsNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> p), new SyncFlightMessage(serverPlayer.getId(), flight)),
                () -> WingsNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> serverPlayer), new SyncFlightMessage(serverPlayer.getId(), flight))
            );
            flight.registerSyncListener(players -> players.notify(notifier));
        }
        event.addCapability(Flights.KEY, provider);
        event.addListener(provider::invalidate);
    }

    @SubscribeEvent
    public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            final Flight flight = Flights.get(event.player);
            if (flight != null) {
                flight.tick(event.player, WingsHelper.findWings(event.player));
            }
        }
    }

    @SubscribeEvent
    public static void onEntityMount(final EntityMountEvent event) {
        if (event.isMounting()) {
            Flights.ifPlayer(event.getEntityMounting(), (player, flight) -> {
                if (flight.isFlying()) {
                    event.setCanceled(true);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(final LivingDeathEvent event) {
        Flights.ifPlayer(event.getEntity(), (player, flight) -> flight.setIsFlying(false, Flight.PlayerSet.ofAll()));
    }

    @SubscribeEvent
    public static void onClone(final PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            return;
        }
        event.getOriginal().reviveCaps();
        final Flight old = Flights.get(event.getOriginal());
        final Flight now = Flights.get(event.getEntity());
        if (old != null && now != null) {
            now.clone(old);
        }
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onLoggedIn(final PlayerEvent.PlayerLoggedInEvent event) {
        syncSelf(event.getEntity());
    }

    @SubscribeEvent
    public static void onRespawn(final PlayerEvent.PlayerRespawnEvent event) {
        syncSelf(event.getEntity());
    }

    @SubscribeEvent
    public static void onChangedDimension(final PlayerEvent.PlayerChangedDimensionEvent event) {
        syncSelf(event.getEntity());
    }

    private static void syncSelf(final Player player) {
        Flights.ifPlayer(player, (p, flight) -> flight.sync(Flight.PlayerSet.ofSelf()));
    }

    @SubscribeEvent
    public static void onStartTracking(final PlayerEvent.StartTracking event) {
        if (event.getEntity() instanceof ServerPlayer tracker) {
            Flights.ifPlayer(event.getTarget(), (p, flight) -> flight.sync(Flight.PlayerSet.ofPlayer(tracker)));
        }
    }

    /** Clic droit avec une fiole sur une chauve-souris : récupère du sang de chauve-souris. */
    @SubscribeEvent
    public static void onEntityInteract(final PlayerInteractEvent.EntityInteract event) {
        final Player player = event.getEntity();
        final InteractionHand hand = event.getHand();
        final ItemStack stack = player.getItemInHand(hand);
        if (WingsConfig.batBloodEnabled() && event.getTarget() instanceof Bat && stack.is(Items.GLASS_BOTTLE)) {
            player.level().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
            final ItemStack batBlood = new ItemStack(WingsItems.BAT_BLOOD.get());
            if (!player.level().isClientSide) {
                player.awardStat(Stats.ITEM_USED.get(Items.GLASS_BOTTLE));
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, batBlood);
                } else if (!player.getInventory().add(batBlood)) {
                    player.drop(batBlood, false);
                }
            }
            event.setCancellationResult(InteractionResult.sidedSuccess(player.level().isClientSide));
            event.setCanceled(true);
        }
    }
}
