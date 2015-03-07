package com.pipai.wf.battle.attack;

public class SimpleRangedAttack extends Attack {
	
	public float getAccuracy(float distance) {
		return 1f;
	}
	
	public float getCritPercentage(float distance) {
		return 0f;
	}
	
	public boolean rollToHit(float distance) {
		return rng.nextFloat() <= getAccuracy(distance);
	}
	
	public int getMinDamage() {
		return 2;
	}
	
	public int getMaxDamage() {
		return 4;
	}
	
	public int damageRoll() {
		return rng.nextInt(getMaxDamage() - getMinDamage()) + getMinDamage();
	}
	
	public String description() {
		return "A simple ranged attack for testing purposes";
	}
	
}
