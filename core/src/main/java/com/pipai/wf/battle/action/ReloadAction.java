package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.exception.IllegalActionException;

public class ReloadAction extends Action {
	
	public ReloadAction(Agent performerAgent) {
		super(performerAgent);
	}
	
	public void perform() throws IllegalActionException {
		super.perform();
		getPerformer().reload();
	}

	public int getAPRequired() { return 1; }
	
}
