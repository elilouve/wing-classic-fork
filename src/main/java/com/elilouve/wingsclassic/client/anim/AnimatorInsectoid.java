package com.elilouve.wingsclassic.client.anim;

import com.elilouve.wingsclassic.util.WingsMath;
import net.minecraft.world.phys.Vec3;

/* Animation portée du mod Wings 1.12.2 (MIT, Pau101). */


public final class AnimatorInsectoid implements Animator {
	private static final float IDLE_FLAP_RATE = 0.05F;

	private static final float LIFT_FLAP_RATE = 1.2F;

	private float targetFlapRate = IDLE_FLAP_RATE;

	private float flapRate;

	private static final float IDLE_AMPLITUDE = 18.0F;
	private static final float FLY_AMPLITUDE = 35.0F;
	private static final float IDLE_FOLD = -8.0F;
	private float targetAmplitude = IDLE_AMPLITUDE;
	private float amplitude = IDLE_AMPLITUDE;
	private float targetFold = IDLE_FOLD;
	private float fold = IDLE_FOLD;
	private float prevFlapCycle;

	private float flapCycle;

	@Override
	public void beginLand() {
		this.beginLift();
	}

	@Override
	public void beginGlide() {
		this.beginLift();
	}

	@Override
	public void beginIdle() {
		this.targetFlapRate = IDLE_FLAP_RATE;
		this.targetAmplitude = IDLE_AMPLITUDE;
		this.targetFold = IDLE_FOLD;
	}

	@Override
	public void beginLift() {
		this.targetFlapRate = LIFT_FLAP_RATE;
		this.targetAmplitude = FLY_AMPLITUDE;
		this.targetFold = 0.0F;
	}

	@Override
	public void beginFall() {
		this.beginIdle();
	}

	public Vec3 getRotation(final float delta) {
		return new Vec3(0.0D, Math.sin(WingsMath.lerp(this.prevFlapCycle, this.flapCycle, delta)) * this.amplitude - 42.0D + this.fold, 0.0D);
	}

	@Override
	public void update() {
		this.prevFlapCycle = this.flapCycle;
		this.flapCycle += this.flapRate;
		this.flapRate += (this.targetFlapRate - this.flapRate) * 0.4F;
		this.amplitude += (this.targetAmplitude - this.amplitude) * 0.15F;
		this.fold += (this.targetFold - this.fold) * 0.15F;
	}
}
