package com.pipai.wf.battle.attack;

import java.util.Random;

public abstract class Attack {
	
	public final Random rng = new Random();
	
	public abstract float getAccuracy(float distance);
	
	/*
	 * Returns the conditional probability P(Crit | Hit)
	 */
	public abstract float getCritPercentage(float distance);
	
	public abstract boolean rollToHit(float distance);
	
	public abstract int getMinDamage();
	
	public abstract int getMaxDamage();
	
	public abstract int damageRoll();
	
	public abstract String description();
	
}
