package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.exception.IllegalActionException;

public class SuppressionAction extends TargetedAction {

	public SuppressionAction(Agent performerAgent, Agent targetAgent) {
		super(performerAgent, targetAgent);
	}

	@Override
	protected void performImpl(BattleConfiguration config) throws IllegalActionException {
		Agent a = getPerformer();
		if (a.getCurrentWeapon().currentAmmo() < 2) {
			throw new IllegalActionException("Not enough ammo to suppress");
		}
		Agent target = getTarget();
		a.suppressOther(target);
		a.setAP(0);
		a.getCurrentWeapon().expendAmmo(2);
		log(BattleEvent.targetedActionEvent(a, target, this));
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

	@Override
	public String name() {
		return "Suppression";
	}

	@Override
	public String description() {
		return "Decreases the target's aim, crit chance, and AOE range";
	}

}
