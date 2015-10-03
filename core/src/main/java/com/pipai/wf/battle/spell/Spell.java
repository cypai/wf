package com.pipai.wf.battle.spell;

import com.pipai.wf.battle.action.TargetedActionable;
import com.pipai.wf.misc.HasDescription;
import com.pipai.wf.misc.HasName;

public abstract class Spell implements HasName, HasDescription, TargetedActionable {

	public abstract int requiredMP();

	public abstract int getMinDamage();

	public abstract int getMaxDamage();

	public abstract boolean canTargetAgent();

	public abstract boolean canOverwatch();

	public abstract SpellElement element();

}
