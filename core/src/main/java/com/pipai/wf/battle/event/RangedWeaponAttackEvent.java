package com.pipai.wf.battle.event;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.item.weapon.Weapon;

public class RangedWeaponAttackEvent extends PerformerTargetEvent {

	public final Weapon weapon;
	public final DamageResult damageResult;

	public RangedWeaponAttackEvent(Agent performer, Agent target, Weapon weapon, DamageResult damageResult) {
		super(performer, target);
		this.weapon = weapon;
		this.damageResult = damageResult;
	}

	@Override
	public String toString() {
		return performer.getName() + " attacked " + target.getName() + " with " + weapon.getName() + " with result: [ " + damageResult + " ]";
	}

}
