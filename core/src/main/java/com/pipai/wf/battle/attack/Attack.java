package com.pipai.wf.battle.attack;

import java.util.Random;

public abstract class Attack {
	
	public static final Random rng = new Random();
	
	public abstract float getAccuracy(float distance, float accuracyModifier);
	
	public float getAccuracy(float distance) {
		return getAccuracy(distance, 1f);
	}
	
	/*
	 * Returns the conditional probability P(Crit | Hit)
	 */
	public abstract float getCritPercentage(float distance, float critModifier);
	
	public float getCritPercentage(float distance) {
		return getCritPercentage(distance, 1f);
	}
	
	public abstract boolean rollToHit(float distance, float accuracyModifier);
	
	public boolean rollToHit(float distance) {
		return rollToHit(distance, 1f);
	}
	
	public abstract int getMinDamage();
	
	public abstract int getMaxDamage();
	
	public abstract int damageRoll(float distance, float critModifier);
	
	public int damageRoll(float distance) {
		return damageRoll(distance, 1f);
	}
	
	public abstract String description();
	
}
