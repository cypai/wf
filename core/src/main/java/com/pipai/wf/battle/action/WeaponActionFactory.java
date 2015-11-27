package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.overwatch.OverwatchActivatedActionSchema;
import com.pipai.wf.battle.weapon.SpellWeapon;
import com.pipai.wf.battle.weapon.Weapon;

public class WeaponActionFactory {

	private BattleController controller;

	public WeaponActionFactory(BattleController controller) {
		this.controller = controller;
	}

	public TargetedWithAccuracyActionOWCapable defaultWeaponAction(Agent performer, Agent target) {
		Weapon weapon = performer.getCurrentWeapon();
		if (weapon instanceof SpellWeapon) {
			return new TargetedSpellWeaponAction(controller, performer, target);
		} else {
			return new RangedWeaponAttackAction(controller, performer, target);
		}
	}

	public OverwatchActivatedActionSchema defaultWeaponActionSchema(Agent performer) {
		Weapon weapon = performer.getCurrentWeapon();
		if (weapon instanceof SpellWeapon) {
			return new OverwatchActivatedActionSchema(TargetedSpellWeaponAction.class);
		} else {
			return new OverwatchActivatedActionSchema(RangedWeaponAttackAction.class);
		}
	}

	public String defaultWeaponActionName(Agent performer) {
		Weapon weapon = performer.getCurrentWeapon();
		if (weapon instanceof SpellWeapon) {
			return new TargetedSpellWeaponAction(controller, performer, null).getName();
		} else {
			return new RangedWeaponAttackAction(controller, performer, null).getName();
		}
	}

}
