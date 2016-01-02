package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.spell.FireballSpell;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.unit.ability.component.SpellAbilityComponent;

public class FireballAbility extends Ability implements SpellAbilityComponent {

	private static final Spell SPELL = new FireballSpell();

	@Override
	public Spell grantedSpell() {
		return SPELL;
	}

	@Override
	public Ability clone() {
		return new FireballAbility();
	}

	@Override
	public String getName() {
		return SPELL.getName();
	}

	@Override
	public String getDescription() {
		return SPELL.getDescription();
	}

}
