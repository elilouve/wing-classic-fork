package com.elilouve.wingsclassic.client.view;

import com.elilouve.wingsclassic.client.anim.Animator;
import com.elilouve.wingsclassic.client.anim.state.State;
import com.elilouve.wingsclassic.client.anim.state.StateIdle;
import com.elilouve.wingsclassic.client.render.FireWingsEffects;
import com.elilouve.wingsclassic.config.ClientConfig;
import com.elilouve.wingsclassic.registry.WingsItems;
import com.elilouve.wingsclassic.flight.Flight;
import com.elilouve.wingsclassic.flight.WingsHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

/** Vue des ailes d'un joueur : suit l'état de vol pour animer les ailes. */
public final class FlightViewDefault implements FlightView {
    private final Player player;
    private final Flight flight;
    private Item item = Items.AIR;
    private Strategy strategy = null;
    private final WingMotion motion = new WingMotion();

    public FlightViewDefault(final Player player, final Flight flight) {
        this.player = player;
        this.flight = flight;
    }

    @Override
    public void ifFormPresent(final Consumer<FormRenderer> consumer) {
        if (this.strategy != null) {
            this.strategy.ifFormPresent(consumer);
        }
    }

    @Override
    public void tick(final ItemStack wings) {
        if (wings.isEmpty()) {
            this.item = Items.AIR;
            this.strategy = null;
            return;
        }
        if (!this.item.equals(wings.getItem())) {
            this.item = wings.getItem();
            final WingForm<?> form = WingForms.get(this.item);
            this.strategy = form == null ? null : new Strategy(form);
        }
        this.motion.tick(this.player);
        if (this.strategy != null) {
            this.strategy.update();
            if (WingsItems.FIRE_WINGS.get().equals(this.item)) {
                FireWingsEffects.spawn(this.player, this.flight);
            }
        }
    }

    private final class Strategy {
        private final WingForm<? extends Animator> form;
        private final Animator animator;
        private State state = new StateIdle();

        Strategy(final WingForm<? extends Animator> form) {
            this.form = form;
            this.animator = form.createAnimator();
        }

        void update() {
            this.animator.update();
            final Player p = FlightViewDefault.this.player;
            final State next = this.state.update(
                FlightViewDefault.this.flight,
                p.getX() - p.xo,
                p.getY() - p.yo,
                p.getZ() - p.zo,
                p,
                WingsHelper.findWings(p)
            );
            if (!this.state.equals(next)) {
                next.beginAnimation(this.animator);
            }
            this.state = next;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        void ifFormPresent(final Consumer<FormRenderer> consumer) {
            final WingForm raw = this.form;
            final Animator anim = this.animator;
            consumer.accept(new FormRenderer() {
                @Override
                public ResourceLocation texture() {
                    return raw.texture();
                }

                @Override
                public boolean fire() {
                    return raw.fire();
                }

                @Override
                public void render(final float delta, final PoseStack pose, final VertexConsumer buf, final int light, final int overlay, final float alpha) {
                    final float strength = ClientConfig.wingPhysics();
                    final float flap = FlightViewDefault.this.flight.isFlying() ? 0.0F : FlightViewDefault.this.motion.flap(delta, strength);
                    raw.model().render(anim, delta, pose, buf, light, overlay, flap, alpha);
                }
            });
        }
    }
}
