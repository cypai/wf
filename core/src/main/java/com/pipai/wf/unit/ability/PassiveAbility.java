package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.spell.Spell;

public abstract class PassiveAbility extends Ability {

	public PassiveAbility(int level) {
		super(level);
	}

	@Override
	public final boolean grantsSpell() {
		return false;
	}

	@Override
	public final Spell getGrantedSpell() {
		return null;
	}

}
