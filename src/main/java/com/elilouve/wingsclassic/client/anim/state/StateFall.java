package com.elilouve.wingsclassic.client.anim.state;

import com.elilouve.wingsclassic.client.anim.Animator;

public final class StateFall extends State {
    public StateFall() {
        super(Animator::beginFall);
    }

    @Override
    protected State createFall() {
        return this;
    }
}
