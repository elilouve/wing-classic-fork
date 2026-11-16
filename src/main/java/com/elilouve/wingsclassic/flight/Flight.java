package com.elilouve.wingsclassic.flight;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

/** Etat de vol d'un joueur (capacité). Adapté du mod Wings d'origine (MIT, Pau101). */
public interface Flight {
    default void setIsFlying(final boolean isFlying) {
        this.setIsFlying(isFlying, PlayerSet.empty());
    }

    void setIsFlying(boolean isFlying, PlayerSet players);

    boolean isFlying();

    default void toggleIsFlying(final PlayerSet players) {
        this.setIsFlying(!this.isFlying(), players);
    }

    void setTimeFlying(int timeFlying);

    int getTimeFlying();

    float getFlyingAmount(float delta);

    void registerFlyingListener(FlyingListener listener);

    void registerSyncListener(SyncListener listener);

    /** Vrai la première fois seulement : sert à ne brancher les écouteurs côté client qu'une fois. */
    boolean claimClientHook();

    boolean canFly(Player player);

    boolean canLand(Player player, ItemStack wings);

    void tick(Player player, ItemStack wings);

    void clone(Flight other);

    void sync(PlayerSet players);

    void serialize(FriendlyByteBuf buf);

    void deserialize(FriendlyByteBuf buf);

    interface FlyingListener {
        void onChange(boolean isFlying);
    }

    interface SyncListener {
        void onSync(PlayerSet players);
    }

    interface PlayerSet {
        void notify(Notifier notifier);

        static PlayerSet empty() {
            return n -> {};
        }

        static PlayerSet ofSelf() {
            return Notifier::notifySelf;
        }

        static PlayerSet ofPlayer(final ServerPlayer player) {
            return n -> n.notifyPlayer(player);
        }

        static PlayerSet ofOthers() {
            return Notifier::notifyOthers;
        }

        static PlayerSet ofAll() {
            return n -> {
                n.notifySelf();
                n.notifyOthers();
            };
        }
    }

    interface Notifier {
        void notifySelf();

        void notifyPlayer(ServerPlayer player);

        void notifyOthers();

        static Notifier of(final Runnable notifySelf, final Consumer<ServerPlayer> notifyPlayer, final Runnable notifyOthers) {
            return new Notifier() {
                @Override
                public void notifySelf() {
                    notifySelf.run();
                }

                @Override
                public void notifyPlayer(final ServerPlayer player) {
                    notifyPlayer.accept(player);
                }

                @Override
                public void notifyOthers() {
                    notifyOthers.run();
                }
            };
        }
    }
}
