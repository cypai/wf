package com.pipai.wf.item.weapon;

import com.google.common.collect.ImmutableSet;
import com.pipai.wf.battle.BattleConfiguration;

public class Pistol extends Weapon {

	public static final int BASE_AMMO = 2;

	private static final ImmutableSet<WeaponFlag> FLAGS = ImmutableSet.<WeaponFlag>of(
			WeaponFlag.NEEDS_AMMUNITION, WeaponFlag.OVERWATCH);

	public Pistol() {
		super(BASE_AMMO);
	}

	@Override
	public int flatAimModifier() {
		return -5;
	}

	@Override
	public int rangeAimModifier(float distance, BattleConfiguration config) {
		if (distance <= 5) {
			return 10;
		}
		return 0;
	}

	@Override
	public int flatCritProbabilityModifier() {
		return -10;
	}

	@Override
	public int rangeCritModifier(float distance, BattleConfiguration config) {
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
	public int baseAmmoCapacity() {
		return BASE_AMMO;
	}

	@Override
	public String getName() {
		return "Pistol";
	}

	@Override
	public String getDescription() {
		return "A typical pistol";
	}

	@Override
	public boolean hasFlag(WeaponFlag flag) {
		return FLAGS.contains(flag);
	}

	@Override
	public Pistol copyAsNew() {
		return new Pistol();
	}

}
