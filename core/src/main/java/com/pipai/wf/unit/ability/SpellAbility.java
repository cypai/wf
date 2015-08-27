package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.spell.Spell;

public abstract class SpellAbility extends Ability {

	public SpellAbility(AbilityType t, int level) {
		super(t, level);
	}
	
	@Override
	public final String name() {
		return getGrantedSpell().name();
	}

	@Override
	public final String description() {
		return getGrantedSpell().description();
	}
	
	@Override
	public final boolean grantsSpell() {
		return true;
	}
	
	@Override
	public abstract Spell getGrantedSpell();
	
}
