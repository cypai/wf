package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.spell.Spell;

public abstract class SpellAbility extends Ability {

	public SpellAbility(int level) {
		super(level);
	}

	@Override
	public final String name() {
		return grantedSpell().name();
	}

	@Override
	public final String description() {
		return grantedSpell().description();
	}

	@Override
	public final boolean grantsSpell() {
		return true;
	}

	@Override
	public abstract Spell grantedSpell();

}
