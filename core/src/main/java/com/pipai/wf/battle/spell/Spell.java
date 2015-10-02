package com.pipai.wf.battle.spell;

import com.pipai.wf.battle.action.TargetedActionable;

public abstract class Spell implements TargetedActionable {

	public abstract int requiredMP();

	public abstract int getMinDamage();

	public abstract int getMaxDamage();

	public abstract String name();

	public abstract String description();

	public abstract boolean canTargetAgent();

	public abstract boolean canOverwatch();

	public abstract SpellElement element();

}
