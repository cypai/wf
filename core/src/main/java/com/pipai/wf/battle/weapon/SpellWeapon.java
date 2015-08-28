package com.pipai.wf.battle.weapon;

import com.pipai.wf.battle.spell.Spell;

public class SpellWeapon extends Weapon {
	
	protected Spell spell;
	
	public SpellWeapon() {
		currentAmmo = 0;
	}
	
	public boolean needsAmmunition() {
		return false;
	}
	
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
	public int situationalAimModifier(float distance, boolean flanked) {
		return 0;
	}

	@Override
	public int flatCritProbabilityModifier() {
		return 0;
	}

	@Override
	public int situationalCritProbabilityModifier(float distance, boolean flanked) {
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
