package com.pipai.wf.battle.weapon;

public class InnateCasting extends SpellWeapon {

	@Override
	public boolean needsAmmunition() {
		return false;
	}

	@Override
	public int baseAmmoCapacity() {
		return 1;
	}

	@Override
	public String getName() {
		return "Casting";
	}

}
