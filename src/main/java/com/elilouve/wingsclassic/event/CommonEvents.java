package com.elilouve.wingsclassic.event;

import com.elilouve.wingsclassic.WingsClassic;
import com.elilouve.wingsclassic.config.WingsConfig;
import com.elilouve.wingsclassic.flight.Flight;
import com.elilouve.wingsclassic.flight.Flights;
import com.elilouve.wingsclassic.flight.WingsHelper;
import com.elilouve.wingsclassic.registry.WingsItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = WingsClassic.ID)
public final class CommonEvents {
    private CommonEvents() {}

    @SubscribeEvent
    public static void onPlayerTick(final PlayerTickEvent.Post event) {
        final Player player = event.getEntity();
        final Flight flight = Flights.get(player);
        if (flight != null) {
            flight.tick(player, WingsHelper.findWings(player));
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
