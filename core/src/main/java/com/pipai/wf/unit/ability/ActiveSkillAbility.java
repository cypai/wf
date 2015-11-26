package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.spell.Spell;

public abstract class ActiveSkillAbility extends Ability {

	public ActiveSkillAbility(int level) {
		super(level);
	}

	@Override
	public final boolean grantsSpell() {
		return false;
	}

	@Override
	public final Spell grantedSpell() {
		return null;
	}

	// Any GUI related ability functions to be added below

}
