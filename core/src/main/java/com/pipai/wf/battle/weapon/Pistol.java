package com.pipai.wf.battle.weapon;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.action.RangedWeaponAttackAction;
import com.pipai.wf.battle.action.TargetedAction;
import com.pipai.wf.battle.action.TargetedActionable;
import com.pipai.wf.battle.agent.Agent;

public class Pistol extends Weapon implements TargetedActionable {

	public static final int BASE_AMMO = 2;

	public Pistol(BattleConfiguration config) {
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
	public boolean needsAmmunition() {
		return true;
	}

	@Override
	public int baseAmmoCapacity() {
		return BASE_AMMO;
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
