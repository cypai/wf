package com.pipai.wf.battle.action;

import com.pipai.wf.battle.Agent;

public abstract class Action {
	
	protected Agent performerAgent;
	
	public Action(Agent performerAgent) {
		this.performerAgent = performerAgent;
	}
	
	public void perform() {
	}
	
	public abstract int getAPRequired();
	
}
