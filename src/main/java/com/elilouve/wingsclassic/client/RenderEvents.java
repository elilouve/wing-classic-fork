package com.elilouve.wingsclassic.client;

import com.elilouve.wingsclassic.WingsClassic;
import com.elilouve.wingsclassic.client.render.WingsLayer;
import com.elilouve.wingsclassic.client.view.FlightViews;
import com.elilouve.wingsclassic.config.ClientConfig;
import com.elilouve.wingsclassic.flight.Flights;
import com.elilouve.wingsclassic.flight.WingsHelper;
import com.elilouve.wingsclassic.util.WingsMath;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class RenderEvents {
    private RenderEvents() {}

    @EventBusSubscriber(modid = WingsClassic.ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static final class ModBus {
        private ModBus() {}

        @SubscribeEvent
        @SuppressWarnings({"unchecked", "rawtypes"})
        public static void onAddLayers(final EntityRenderersEvent.AddLayers event) {
            for (final PlayerSkin.Model skin : event.getSkins()) {
                final LivingEntityRenderer living = event.getSkin(skin);
                if (living != null && living.getModel() instanceof HumanoidModel) {
                    living.addLayer(new WingsLayer(living));
                }
            }
            for (final EntityType<?> type : event.getEntityTypes()) {
                if (type == EntityType.PLAYER) {
                    continue;
                }
                final EntityRenderer<?> renderer = event.getRenderer((EntityType) type);
                if (renderer instanceof LivingEntityRenderer living && living.getModel() instanceof HumanoidModel) {
                    living.addLayer(new WingsLayer(living));
                }
            }
        }
    }

    @EventBusSubscriber(modid = WingsClassic.ID, value = Dist.CLIENT)
    public static final class ForgeBus {
        private ForgeBus() {}

        /** Fait avancer l'animation des ailes de chaque joueur à chaque tick. */
        @SubscribeEvent
        public static void onPlayerTick(final PlayerTickEvent.Post event) {
            if (event.getEntity() instanceof AbstractClientPlayer player) {
                FlightViews.get(player).tick(WingsHelper.findWings(player));
            }
        }

        /** Inclinaison de la caméra pendant le vol. */
        @SubscribeEvent
        public static void onCameraAngles(final ViewportEvent.ComputeCameraAngles event) {
            if (!ClientConfig.cameraRoll()) {
                return;
            }
            Flights.ifPlayer(event.getCamera().getEntity(), (player, flight) -> {
                final float delta = (float) event.getPartialTick();
                final float amt = flight.getFlyingAmount(delta);
                if (amt > 0.0F) {
                    final float roll = com.elilouve.wingsclassic.client.render.FlightPose.bodyRoll(player, delta);
                    event.setRoll(net.minecraft.util.Mth.clamp(-roll * 0.25F * amt, -20.0F, 20.0F));
                }
            });
        }
    }
}
