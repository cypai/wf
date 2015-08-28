package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
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
		
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

}