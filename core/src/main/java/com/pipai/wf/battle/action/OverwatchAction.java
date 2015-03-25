package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.attack.Attack;

public class OverwatchAction extends Action {
	
	private Attack atk;
	
	public OverwatchAction(Agent performerAgent, Attack attack) {
		super(performerAgent);
		atk = attack;
	}
	
	public void perform() {
		super.perform();
		this.performerAgent.overwatch(atk);
	}

	public int getAPRequired() { return 1; }
	
}
