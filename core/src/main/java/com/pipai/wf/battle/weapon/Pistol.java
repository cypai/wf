package com.pipai.wf.battle.weapon;

import com.pipai.wf.battle.action.RangedWeaponAttackAction;
import com.pipai.wf.battle.action.TargetedAction;
import com.pipai.wf.battle.action.TargetedActionable;
import com.pipai.wf.battle.agent.Agent;

public class Pistol extends Weapon implements TargetedActionable {

	@Override
	public int flatAimModifier() {
		return -5;
	}

	@Override
	public int rangeAimModifier(float distance) {
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
	public int rangeCritModifier(float distance) {
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

	@Override
	public TargetedAction getAction(Agent performer, Agent target) {
		return new RangedWeaponAttackAction(performer, target);
	}

}
