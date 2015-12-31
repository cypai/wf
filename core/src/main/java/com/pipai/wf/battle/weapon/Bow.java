package com.pipai.wf.battle.weapon;

import com.google.common.collect.ImmutableSet;
import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.RangedWeaponAttackAction;
import com.pipai.wf.battle.action.TargetedAction;
import com.pipai.wf.battle.action.TargetedActionable;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.math.LinearFunction;
import com.pipai.wf.unit.ability.QuickReloadAbility;

public class Bow extends Weapon implements TargetedActionable {

	public static final int BASE_AMMO = 3;

	private static final ImmutableSet<WeaponFlag> FLAGS = ImmutableSet.<WeaponFlag> of(
			WeaponFlag.BOW, WeaponFlag.NEEDS_AMMUNITION, WeaponFlag.OVERWATCH, WeaponFlag.PRECISE_SHOT, WeaponFlag.SUPPRESSION);

	public Bow() {
		super(BASE_AMMO);
		init();
	}

	private void init() {
		addGrantedAbility(new QuickReloadAbility());
	}

	@Override
	public int flatAimModifier() {
		return 0;
	}

	@Override
	public int rangeAimModifier(float distance, BattleConfiguration config) {
		float range = config.sightRange();
		int slightBonusMax = 5;
		float slightBonusRange = range * 0.7f;
		int betterBonusMax = 15;
		float betterBonusRange = range * 0.5f;
		int goodBonusMax = 30;
		float goodBonusRange = range * 0.3f;
		if (distance <= slightBonusRange && distance > betterBonusRange) {
			return (int) (new LinearFunction(slightBonusRange, 0, betterBonusRange, slightBonusMax)).eval(distance);
		} else if (distance <= betterBonusRange && distance > goodBonusRange) {
			return (int) (new LinearFunction(betterBonusRange, slightBonusMax, goodBonusRange, betterBonusMax)).eval(distance);
		} else if (distance <= goodBonusRange) {
			return (int) (new LinearFunction(goodBonusRange, betterBonusMax, 0, goodBonusMax)).eval(distance);
		}
		return 0;
	}

	@Override
	public int flatCritProbabilityModifier() {
		return 10;
	}

	@Override
	public int rangeCritModifier(float distance, BattleConfiguration config) {
		return 0;
	}

	@Override
	public int minBaseDamage() {
		return 2;
	}

	@Override
	public int maxBaseDamage() {
		return 4;
	}

	@Override
	public int baseAmmoCapacity() {
		return BASE_AMMO;
	}

	@Override
	public String getName() {
		return "Bow";
	}

	@Override
	public String getDescription() {
		return "A typical bow";
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
