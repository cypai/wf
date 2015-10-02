package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.exception.IllegalActionException;

public class SwitchWeaponAction extends AlterStateAction {

	public SwitchWeaponAction(Agent performerAgent) {
		super(performerAgent);
	}

	@Override
	public int getAPRequired() {
		return 0;
	}

	@Override
	protected void performImpl(BattleConfiguration config) throws IllegalActionException {
		getPerformer().switchWeapon();
		log(BattleEvent.switchWeaponEvent(getPerformer()));
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
