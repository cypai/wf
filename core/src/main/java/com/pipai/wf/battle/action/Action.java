package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;

public abstract class Action {
	
	protected Agent performerAgent;
	
	public Action(Agent performerAgent) {
		this.performerAgent = performerAgent;
	}
	
	public void perform() {
	}
	
	/*
	 * Returns the minimum AP required to perform the action
	 */
	public abstract int getAPRequired();
	
}
