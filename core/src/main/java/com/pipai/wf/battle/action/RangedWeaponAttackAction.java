package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.DamageCalculator;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.damage.WeaponDamageFunction;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.util.UtilFunctions;

public class RangedWeaponAttackAction extends TargetedWithAccuracyAction {

	public RangedWeaponAttackAction(Agent performerAgent, Agent targetAgent) {
		super(performerAgent, targetAgent);
	}

	@Override
	public int toHit() {
		Agent a = getPerformer();
		Agent target = getTarget();
		Weapon weapon = a.getCurrentWeapon();
		int total_aim = a.getBaseAim();
		total_aim += weapon.flatAimModifier();
		total_aim += weapon.situationalAimModifier(a.getDistanceFrom(target), target.isFlankedBy(a));
		total_aim -= target.getDefense(a.getPosition());
		return UtilFunctions.clamp(1, 100, total_aim);
	}

	@Override
	public int toCrit() {
		Agent a = getPerformer();
		Agent target = getTarget();
		Weapon weapon = a.getCurrentWeapon();
		int crit_prob = weapon.flatCritProbabilityModifier();
		crit_prob += weapon.situationalCritProbabilityModifier(a.getDistanceFrom(target), target.isFlankedBy(a));
		return UtilFunctions.clamp(1, 100, crit_prob);
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		Agent a = getPerformer();
		Agent target = getTarget();
		Weapon w = a.getCurrentWeapon();
		if (w.needsAmmunition() && w.currentAmmo() == 0) {
			throw new IllegalActionException("Not enough ammo to fire " + w.name());
		}
		DamageResult result = DamageCalculator.rollDamageGeneral(this, new WeaponDamageFunction(w));
		a.setAP(0);
		log(BattleEvent.rangedWeaponAttackEvent(a, target, w, result));
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

}
