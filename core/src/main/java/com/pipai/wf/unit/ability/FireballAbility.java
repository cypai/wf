package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.spell.FireballSpell;
import com.pipai.wf.battle.spell.Spell;

public class FireballAbility extends SpellAbility {

	public FireballAbility() {
		super(0);
	}

	@Override
	public Spell grantedSpell() {
		return new FireballSpell();
	}

	@Override
	public Ability clone() {
		return new FireballAbility();
	}

}
