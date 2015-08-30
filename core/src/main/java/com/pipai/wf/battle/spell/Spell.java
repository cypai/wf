package com.pipai.wf.battle.spell;

import com.pipai.wf.battle.action.TargetedActionable;
import com.pipai.wf.util.UtilFunctions;

public abstract class Spell implements TargetedActionable {

	public abstract int requiredMP();

	public abstract int getMinDamage();

	public abstract int getMaxDamage();

	public int damageRoll() {
		int min_dmg = getMinDamage();
		int max_dmg = getMaxDamage();
		return UtilFunctions.rng.nextInt(max_dmg - min_dmg + 1) + min_dmg;
	}

	public abstract String name();

	public abstract String description();

	public abstract boolean canTargetAgent();

	public abstract boolean canOverwatch();

}
