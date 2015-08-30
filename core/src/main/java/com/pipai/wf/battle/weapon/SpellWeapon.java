package com.pipai.wf.battle.weapon;

import com.pipai.wf.battle.spell.Spell;

public class SpellWeapon extends Weapon {

	protected Spell spell;

	public SpellWeapon() {
		currentAmmo = 0;
	}

	@Override
	public boolean needsAmmunition() {
		return false;
	}

	@Override
	public int baseAmmoCapacity() {
		return 0;
	}

	public void ready(Spell spell) {
		this.spell = spell;
	}

	public void cast() {
		this.spell = null;
	}

	public Spell getSpell() {
		return this.spell;
	}

	@Override
	public int flatAimModifier() {
		return 0;
	}

	@Override
	public int rangeAimModifier(float distance) {
		return 0;
	}

	@Override
	public int flatCritProbabilityModifier() {
		return 0;
	}

	@Override
	public int rangeCritModifier(float distance) {
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
	public String name() {
		return "Spell";
	}

}
