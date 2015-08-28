package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;

public interface TargetedActionable {
	
	public TargetedAction getAction(Agent performer, Agent target);
	
}
