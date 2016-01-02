package com.pipai.wf.battle.weapon;

import com.google.common.collect.ImmutableSet;
import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.RangedWeaponAttackAction;
import com.pipai.wf.battle.action.TargetedAction;
import com.pipai.wf.battle.action.TargetedActionable;
import com.pipai.wf.battle.agent.Agent;

public class Pistol extends Weapon implements TargetedActionable {

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
	public TargetedAction getAction(BattleController controller, Agent performer, Agent target) {
		return new RangedWeaponAttackAction(controller, performer, target);
	}

	@Override
	public boolean hasFlag(WeaponFlag flag) {
		return FLAGS.contains(flag);
	}

}
