package com.pipai.wf.battle.damage;

import com.pipai.wf.spell.Spell;
import com.pipai.wf.util.Rng;

public class SpellDamageFunction extends DamageFunction {

	private Spell spell;

	public SpellDamageFunction(Spell spell) {
		this.spell = spell;
	}

	@Override
	public int rollForDamage(Rng rng) {
		return rng.randInt(spell.getMinDamage(), spell.getMaxDamage());
	}

}
