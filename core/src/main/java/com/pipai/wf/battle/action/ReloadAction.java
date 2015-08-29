package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.exception.IllegalActionException;

public class ReloadAction extends AlterStateAction {
	
	public ReloadAction(Agent performerAgent) {
		super(performerAgent);
	}
	
	public int getAPRequired() { return 1; }

	@Override
	protected void performImpl() throws IllegalActionException {
		getPerformer().reload();
	}

	@Override
	public String name() {
		return "Reload";
	}

	@Override
	public String description() {
		return "Reload your current weapon";
	}
	
}
