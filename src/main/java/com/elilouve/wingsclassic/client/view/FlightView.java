package com.elilouve.wingsclassic.client.view;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

/** Ce que le rendu sait des ailes d'une entité : forme, animation. */
public interface FlightView {
    void ifFormPresent(Consumer<FormRenderer> consumer);

    void tick(ItemStack wings);

    interface FormRenderer {
        ResourceLocation texture();

        boolean fire();

        void render(float delta, PoseStack pose, VertexConsumer buf, int light, int overlay, float alpha);
    }
}
