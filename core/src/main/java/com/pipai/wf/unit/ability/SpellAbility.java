package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.spell.Spell;

public abstract class SpellAbility extends Ability {

	public SpellAbility(int level) {
		super(level);
	}

	@Override
	public final String getName() {
		return grantedSpell().getName();
	}

	@Override
	public final String getDescription() {
		return grantedSpell().getDescription();
	}

	@Override
	public final boolean grantsSpell() {
		return true;
	}

	@Override
	public abstract Spell grantedSpell();

}
