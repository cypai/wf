package com.pipai.wf.battle.weapon;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.spell.Spell;

public abstract class SpellWeapon extends Weapon {

	private Spell spell;

	public SpellWeapon(BattleConfiguration config) {
		super(0);
	}

	public void ready(Spell spell) {
		this.spell = spell;
	}

	public void cast() {
		spell = null;
	}

	public Spell getSpell() {
		return spell;
	}

	@Override
	public int flatAimModifier() {
		return 0;
	}

	@Override
	public int rangeAimModifier(float distance, BattleConfiguration config) {
		return 0;
	}

	@Override
	public int flatCritProbabilityModifier() {
		return 0;
	}

	@Override
	public int rangeCritModifier(float distance, BattleConfiguration config) {
		return 0;
	}

	@Override
	public int minBaseDamage() {
		return 0;
	}

	@Override
	public int maxBaseDamage() {
		return 0;
	}

}
