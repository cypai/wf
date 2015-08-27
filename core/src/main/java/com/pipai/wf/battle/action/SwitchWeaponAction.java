package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.exception.IllegalActionException;

public class SwitchWeaponAction extends Action {
	
	public SwitchWeaponAction(Agent performerAgent) {
		super(performerAgent);
	}
	
	public void perform() throws IllegalActionException {
		super.perform();
		getPerformer().switchWeapon();
	}

	public int getAPRequired() { return 0; }
	
}
