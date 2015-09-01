package com.pipai.wf.battle.weapon;

import com.pipai.wf.battle.action.RangedWeaponAttackAction;
import com.pipai.wf.battle.action.TargetedAction;
import com.pipai.wf.battle.action.TargetedActionable;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.config.WFConfig;
import com.pipai.wf.math.LinearFunction;
import com.pipai.wf.unit.ability.AbilityList;
import com.pipai.wf.unit.ability.QuickReloadAbility;

public class Bow extends Weapon implements TargetedActionable {

	@Override
	public int flatAimModifier() {
		return 0;
	}

	@Override
	public int rangeAimModifier(float distance) {
		float range = WFConfig.battleProps().sightRange();
		int slightBonusMax = 5;
		float slightBonusRange = range * 0.7f;
		int betterBonusMax = 15;
		float betterBonusRange = range * 0.5f;
		int goodBonusMax = 30;
		float goodBonusRange = range * 0.3f;
		if (distance <= slightBonusRange && distance > betterBonusRange) {
			return (int)(new LinearFunction(slightBonusRange, 0, betterBonusRange, slightBonusMax)).eval(distance);
		} else if (distance <= betterBonusRange && distance > goodBonusRange) {
			return (int)(new LinearFunction(betterBonusRange, slightBonusMax, goodBonusRange, betterBonusMax)).eval(distance);
		} else if (distance <= goodBonusRange) {
			return (int)(new LinearFunction(goodBonusRange, betterBonusMax, 0, goodBonusMax)).eval(distance);
		}
		return 0;
	}

	@Override
	public int flatCritProbabilityModifier() {
		return 10;
	}

	@Override
	public int rangeCritModifier(float distance) {
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
	public boolean needsAmmunition() {
		return true;
	}

	@Override
	public int baseAmmoCapacity() {
		return 3;
	}

	@Override
	public String name() {
		return "Bow";
	}

	@Override
	public TargetedAction getAction(Agent performer, Agent target) {
		return new RangedWeaponAttackAction(performer, target);
	}

	@Override
	public AbilityList getGrantedAbilities() {
		AbilityList alist = new AbilityList();
		alist.add(new QuickReloadAbility());
		return alist;
	}

}
