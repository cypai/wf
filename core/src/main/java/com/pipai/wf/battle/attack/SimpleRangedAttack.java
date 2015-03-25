package com.pipai.wf.battle.attack;

public class SimpleRangedAttack extends Attack {
	
	public float getAccuracy(float distance, float accuracyModifier) {
		return 1f * accuracyModifier;
	}
	
	public float getCritPercentage(float distance, float critModifier) {
		return 0f;
	}
	
	public boolean rollToHit(float distance, float accuracyModifier) {
		return rng.nextFloat() <= getAccuracy(distance, accuracyModifier);
	}
	
	public int getMinDamage() {
		return 2;
	}
	
	public int getMaxDamage() {
		return 4;
	}
	
	public int damageRoll(float distance, float critModifier) {
		return rng.nextInt(getMaxDamage() - getMinDamage() + 1) + getMinDamage();
	}
	
	public String description() {
		return "A simple ranged attack for testing purposes";
	}
	
}
