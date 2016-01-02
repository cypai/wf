package com.pipai.wf.battle.weapon;

import com.google.common.collect.ImmutableSet;

public class InnateCasting extends SpellWeapon {

	private static final ImmutableSet<WeaponFlag> FLAGS = ImmutableSet.<WeaponFlag>of(WeaponFlag.OVERWATCH);

	@Override
	public int baseAmmoCapacity() {
		return 1;
	}

	@Override
	public String getName() {
		return "Casting";
	}

	@Override
	public String getDescription() {
		return "Enables casting without a physical spell weapon";
	}

	@Override
	public boolean hasFlag(WeaponFlag flag) {
		return FLAGS.contains(flag);
	}

}
