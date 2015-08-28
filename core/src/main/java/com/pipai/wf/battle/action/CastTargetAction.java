package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.exception.IllegalActionException;

public class CastTargetAction extends TargetedAction {
	
	public CastTargetAction(Agent performerAgent, Agent target) {
		super(performerAgent, target);
	}
	
	public int getAPRequired() { return 1; }

	@Override
	protected void performImpl() throws IllegalActionException {
		getPerformer().castReadiedSpell(getTarget());
	}
	
}
