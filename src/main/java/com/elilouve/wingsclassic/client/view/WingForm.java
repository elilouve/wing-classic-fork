package com.elilouve.wingsclassic.client.view;

import com.elilouve.wingsclassic.client.anim.Animator;
import com.elilouve.wingsclassic.client.model.WingsModel;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

/** Une « forme » d'ailes : son animateur, son modèle et sa texture. */
public final class WingForm<A extends Animator> {
    private final Supplier<A> animator;
    private final WingsModel<A> model;
    private final ResourceLocation texture;
    private final boolean fire;

    public WingForm(final Supplier<A> animator, final WingsModel<A> model, final ResourceLocation texture) {
        this(animator, model, texture, false);
    }

    public WingForm(final Supplier<A> animator, final WingsModel<A> model, final ResourceLocation texture, final boolean fire) {
        this.fire = fire;
        this.animator = animator;
        this.model = model;
        this.texture = texture;
    }

    public A createAnimator() {
        return this.animator.get();
    }

    public WingsModel<A> model() {
        return this.model;
    }

    public boolean fire() {
        return this.fire;
    }

    public ResourceLocation texture() {
        return this.texture;
    }
}
