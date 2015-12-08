package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.exception.IllegalActionException;

public class SwitchWeaponAction extends AlterStateAction {

	public SwitchWeaponAction(BattleController controller, Agent performerAgent) {
		super(controller, performerAgent);
	}

	@Override
	public int getAPRequired() {
		return 0;
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		getPerformer().switchWeapon();
		logBattleEvent(BattleEvent.switchWeaponEvent(getPerformer()));
	}

	@Override
	public String getName() {
		return "Switch Weapon";
	}

	@Override
	public String getDescription() {
		return "Switch to a different weapon";
	}

}
