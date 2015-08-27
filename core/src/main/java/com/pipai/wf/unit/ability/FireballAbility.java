package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.spell.FireballSpell;
import com.pipai.wf.battle.spell.Spell;

public class FireballAbility extends SpellAbility {

	public FireballAbility() {
		super(AbilityType.FIREBALL, 0);
	}

	@Override
	public Spell getGrantedSpell() {
		return new FireballSpell();
	}
	
}
