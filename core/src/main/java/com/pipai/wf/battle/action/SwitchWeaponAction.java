package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.exception.IllegalActionException;

public class SwitchWeaponAction extends AlterStateAction {
	
	public SwitchWeaponAction(Agent performerAgent) {
		super(performerAgent);
	}
	
	public int getAPRequired() { return 0; }

	@Override
	protected void performImpl() throws IllegalActionException {
		getPerformer().switchWeapon();
	}

	@Override
	public String name() {
		return "Switch Weapon";
	}

	@Override
	public String description() {
		return "Switch to a different weapon";
	}
	
}
