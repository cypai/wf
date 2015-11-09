package com.pipai.wf.battle.damage;

public class DamageResult {

	public final int damage, damageReduction;
	public final boolean hit, crit;

	public DamageResult(boolean hit, boolean crit, int damage, int damageReduction) {
		this.hit = hit;
		this.crit = crit;
		this.damage = damage;
		this.damageReduction = damageReduction;
	}

	public static DamageResult missedResult() {
		return new DamageResult(false, false, 0, 0);
	}

}
