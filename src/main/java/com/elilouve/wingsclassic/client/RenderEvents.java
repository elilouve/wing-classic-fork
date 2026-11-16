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
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

public final class RenderEvents {
    private RenderEvents() {}

    @Mod.EventBusSubscriber(modid = WingsClassic.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class ModBus {
        private ModBus() {}

        @SubscribeEvent
        @SuppressWarnings({"unchecked", "rawtypes"})
        public static void onAddLayers(final EntityRenderersEvent.AddLayers event) {
            for (final String skin : event.getSkins()) {
                final LivingEntityRenderer living = event.getSkin(skin);
                if (living != null && living.getModel() instanceof HumanoidModel) {
                    living.addLayer(new WingsLayer(living));
                }
            }
            for (final EntityType<?> type : ForgeRegistries.ENTITY_TYPES.getValues()) {
                if (type == EntityType.PLAYER) {
                    continue;
                }
                final EntityRenderer<?> renderer = event.getEntityRenderer((EntityType) type);
                if (renderer instanceof LivingEntityRenderer living && living.getModel() instanceof HumanoidModel) {
                    living.addLayer(new WingsLayer(living));
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = WingsClassic.ID, value = Dist.CLIENT)
    public static final class ForgeBus {
        private ForgeBus() {}

        /** Fait avancer l'animation des ailes de chaque joueur à chaque tick. */
        @SubscribeEvent
        public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.END && event.player instanceof AbstractClientPlayer player) {
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
