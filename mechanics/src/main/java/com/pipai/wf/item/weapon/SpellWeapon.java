package com.pipai.wf.item.weapon;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.spell.Spell;

public abstract class SpellWeapon extends Weapon {

	private Spell spell;

	public SpellWeapon() {
		super(0);
	}

	public void setSpell(Spell spell) {
		this.spell = spell;
	}

	public Spell getSpell() {
		return spell;
	}

	public void cast() {
		spell = null;
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

	@Override
	public SpellWeapon copy() {
		SpellWeapon copy = (SpellWeapon) copyAsNew();
		copy.setCurrentAmmo(getCurrentAmmo());
		copy.setSpell(getSpell());
		return copy;
	}

}
