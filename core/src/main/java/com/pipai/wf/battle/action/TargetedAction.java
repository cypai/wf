package com.pipai.wf.battle.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;

public abstract class TargetedAction extends Action {

	private static final Logger LOGGER = LoggerFactory.getLogger(TargetedAction.class);

	private Agent target;

	public TargetedAction(BattleController controller, Agent performerAgent, Agent targetAgent) {
		super(controller, performerAgent);
		target = targetAgent;
	}

	public TargetedAction(BattleController controller, Agent performerAgent) {
		super(controller, performerAgent);
		target = null;
	}

	@Override
	protected void postPerform() {
		Agent performer = getPerformer();
		if (performer.getTeam() != target.getTeam() && target.isKO()) {
			int exp = target.getExpGiven();
			LOGGER.debug(target.getName() + " was KOed by " + performer.getName() + ": +" + exp + " EXP");
			performer.giveExp(exp);
		}
	}

	public Agent getTarget() {
		return target;
	}

	public void setTarget(Agent target) {
		this.target = target;
	}

}
