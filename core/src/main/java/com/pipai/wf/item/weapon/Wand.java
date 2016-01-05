package com.pipai.wf.item.weapon;

import com.pipai.wf.spell.Spell;

public class Wand extends SpellWeapon {

	@Override
	public int baseAmmoCapacity() {
		return 1;
	}

	@Override
	public void setSpell(Spell spell) {
		super.setSpell(spell);
		reload();
	}

	@Override
	public void cast() {
		super.cast();
		expendAmmo(1);
	}

	@Override
	public String getName() {
		return "Wand";
	}

	@Override
	public String getDescription() {
		return "A typical wand";
	}

	@Override
	public boolean hasFlag(WeaponFlag flag) {
		return false;
	}

	@Override
	public Wand copyAsNew() {
		return new Wand();
	}

}
