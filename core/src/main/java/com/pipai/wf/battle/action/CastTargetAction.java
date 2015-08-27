package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.exception.IllegalActionException;

public class CastTargetAction extends Action {
	
	protected Agent target;
	
	public CastTargetAction(Agent performerAgent, Agent target) {
		super(performerAgent);
		this.target = target;
	}
	
	public void perform() throws IllegalActionException {
		super.perform();
		getPerformer().castReadiedSpell(this.target);
	}

	public int getAPRequired() { return 1; }
	
}
