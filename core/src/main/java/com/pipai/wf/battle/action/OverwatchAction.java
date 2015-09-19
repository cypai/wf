package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.misc.OverwatchHelper;
import com.pipai.wf.exception.IllegalActionException;

public class OverwatchAction extends AlterStateAction {

	private Class<? extends TargetedWithAccuracyActionOWCapable> owAction;

	public OverwatchAction(Agent performerAgent) {
		super(performerAgent);
		this.owAction = WeaponActionFactory.defaultWeaponActionClass(performerAgent);
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
		performer.overwatch(owAction);
		log(BattleEvent.overwatchEvent(getPerformer(), OverwatchHelper.getName(this)));
	}

	@Override
	public String name() {
		return "Overwatch";
	}

	@Override
	public String description() {
		return "Attack the first enemy that moves in range";
	}

	public Class<? extends TargetedWithAccuracyActionOWCapable> getOWClass() {
		return owAction;
	}

}
