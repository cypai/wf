package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.overwatch.OverwatchActivatedActionSchema;
import com.pipai.wf.battle.overwatch.OverwatchHelper;
import com.pipai.wf.exception.IllegalActionException;

public class OverwatchAction extends AlterStateAction {

	private OverwatchActivatedActionSchema owAction;

	public OverwatchAction(BattleController controller, Agent performerAgent) {
		super(controller, performerAgent);
		owAction = new WeaponActionFactory(controller).defaultWeaponActionSchema(performerAgent);
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		Agent performer = getPerformer();
		if (performer.getCurrentWeapon() == null) {
			throw new IllegalActionException("Not currently wielding a weapon");
		}
		if (performer.getCurrentWeapon().needsAmmunition()) {
			if (performer.getCurrentWeapon().currentAmmo() == 0) {
				throw new IllegalActionException("Not enough ammo to overwatch");
			}
		}
		performer.setOverwatch(owAction);
		log(BattleEvent.overwatchEvent(getPerformer(), OverwatchHelper.getName(this)));
	}

	@Override
	public String getName() {
		return "Overwatch";
	}

	@Override
	public String getDescription() {
		return "Attack the first enemy that moves in range";
	}

	public OverwatchActivatedActionSchema getOverwatchActionSchema() {
		return owAction;
	}

}
