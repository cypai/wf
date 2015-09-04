package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;

public abstract class TargetedAction extends Action {

	private Agent target;

	public TargetedAction(Agent performerAgent, Agent targetAgent) {
		super(performerAgent);
		target = targetAgent;
	}

	public Agent getTarget() {
		return target;
	}

}
