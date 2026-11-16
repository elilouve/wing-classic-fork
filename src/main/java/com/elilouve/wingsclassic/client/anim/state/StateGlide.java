package com.elilouve.wingsclassic.client.anim.state;

import com.elilouve.wingsclassic.client.anim.Animator;

public final class StateGlide extends State {
    private static final int LIFT_STATE_DELAY = 6;

    public StateGlide() {
        super(Animator::beginGlide);
    }

    @Override
    protected State createLift() {
        return new StateLift(LIFT_STATE_DELAY);
    }

    @Override
    protected State createGlide() {
        return this;
    }
}
