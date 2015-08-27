package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.spell.Spell;

public abstract class PassiveAbility extends Ability {

	public PassiveAbility(AbilityType t, int level) {
		super(t, level);
	}
	
	public final boolean grantsSpell() {
		return false;
	}
	
	public final Spell getGrantedSpell() {
		return null;
	}

}
