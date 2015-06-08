package com.pipai.wf.battle.weapon;

public class Pistol extends Weapon {

	@Override
	public int flatAimModifier() {
		return -5;
	}

	@Override
	public int situationalAimModifier(float distance, boolean flanked) {
		return 0;
	}

	@Override
	public float flatCritProbabilityModifier() {
		return -10;
	}

	@Override
	public float situationalCritProbabilityModifier(float distance, boolean flanked) {
		return 0;
	}

	@Override
	public int minBaseDamage() {
		return 1;
	}

	@Override
	public int maxBaseDamage() {
		return 3;
	}

	@Override
	public boolean needsAmmunition() {
		return true;
	}

	@Override
	public int baseAmmoCapacity() {
		return 2;
	}
	
	@Override
	public String name() {
		return "Pistol";
	}

}
