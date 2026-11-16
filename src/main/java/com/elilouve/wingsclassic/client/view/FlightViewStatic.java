package com.elilouve.wingsclassic.client.view;

import com.elilouve.wingsclassic.client.anim.Animator;
import com.elilouve.wingsclassic.flight.WingsHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

/** Vue pour les entités autres que les joueurs (ailes immobiles, position de repos). */
public final class FlightViewStatic implements FlightView {
    private final LivingEntity entity;
    private Item item = Items.AIR;
    private WingForm<?> form;
    private Animator animator;

    public FlightViewStatic(final LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void ifFormPresent(final Consumer<FormRenderer> consumer) {
        final ItemStack stack = WingsHelper.findWings(this.entity);
        if (!this.item.equals(stack.getItem())) {
            this.item = stack.getItem();
            this.form = WingForms.get(this.item);
            this.animator = this.form == null ? null : this.form.createAnimator();
        }
        if (this.form == null) {
            return;
        }
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
                raw.model().render(anim, delta, pose, buf, light, overlay, 0.0F, alpha);
            }
        });
    }

    @Override
    public void tick(final ItemStack wings) {}
}
