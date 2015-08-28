package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.attack.Attack;
import com.pipai.wf.exception.IllegalActionException;

public class OverwatchAction extends AlterStateAction {
	
	private Attack atk;
	
	public OverwatchAction(Agent performerAgent, Attack attack) {
		super(performerAgent);
		atk = attack;
	}
	
	public int getAPRequired() { return 1; }

	@Override
	protected void performImpl() throws IllegalActionException {
		getPerformer().overwatch(atk);
	}
	
}
