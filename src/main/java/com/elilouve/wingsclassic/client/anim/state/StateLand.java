package com.elilouve.wingsclassic.client.anim.state;

import com.elilouve.wingsclassic.client.anim.Animator;

public final class StateLand extends State {
    public StateLand() {
        super(Animator::beginLand);
    }

    @Override
    protected State createLand() {
        return this;
    }
}
