package com.pipai.wf.battle.damage;

import com.pipai.wf.battle.spell.Spell;

public class SpellDamageFunction extends DamageFunction {
	
	private Spell spell;
	
	public SpellDamageFunction(Spell spell) {
		this.spell = spell;
	}

	@Override
	public int rollForDamage() {
		return spell.damageRoll();
	}

}
