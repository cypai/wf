package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.spell.FireballSpell;
import com.pipai.wf.battle.spell.Spell;

public class FireballAbility extends SpellAbility {

	public FireballAbility(int level) {
		super(AbilityType.FIREBALL, level);
	}

	@Override
	public Spell grantSpell() {
		return new FireballSpell();
	}
	
}
