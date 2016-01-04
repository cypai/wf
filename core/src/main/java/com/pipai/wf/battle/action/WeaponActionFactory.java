package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.item.weapon.SpellWeapon;
import com.pipai.wf.item.weapon.Weapon;

public class WeaponActionFactory {

	private BattleController controller;

	public WeaponActionFactory(BattleController controller) {
		this.controller = controller;
	}

	public OverwatchableTargetedAction defaultWeaponAction(Agent performer, Agent target) {
		Weapon weapon = performer.getCurrentWeapon();
		if (weapon instanceof SpellWeapon) {
			return new TargetedSpellWeaponAction(controller, performer, target);
		} else {
			return new RangedWeaponAttackAction(controller, performer, target);
		}
	}

	public OverwatchableTargetedAction defaultWeaponActionSchema(Agent performer) {
		Weapon weapon = performer.getCurrentWeapon();
		if (weapon instanceof SpellWeapon) {
			return new TargetedSpellWeaponAction(controller, performer);
		} else {
			return new RangedWeaponAttackAction(controller, performer);
		}
	}

	public String defaultWeaponActionName(Agent performer) {
		Weapon weapon = performer.getCurrentWeapon();
		if (weapon instanceof SpellWeapon) {
			return new TargetedSpellWeaponAction(controller, performer).getName();
		} else {
			return new RangedWeaponAttackAction(controller, performer).getName();
		}
	}

}
