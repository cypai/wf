package com.pipai.wf.battle.damage;

public class DamageResult {

	private final int damage, damageReduction;
	private final boolean hit, crit;

	public DamageResult(boolean hit, boolean crit, int damage, int damageReduction) {
		this.hit = hit;
		this.crit = crit;
		this.damage = damage;
		this.damageReduction = damageReduction;
	}

	public int getDamage() {
		return damage;
	}

	public int getDamageReduction() {
		return damageReduction;
	}

	public boolean isHit() {
		return hit;
	}

	public boolean isCrit() {
		return crit;
	}

	public static DamageResult buildMissedResult() {
		return new DamageResult(false, false, 0, 0);
	}

}
