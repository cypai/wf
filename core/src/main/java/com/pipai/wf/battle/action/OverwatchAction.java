package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.misc.OverwatchHelper;
import com.pipai.wf.exception.IllegalActionException;

public class OverwatchAction extends AlterStateAction {

	private Class<? extends TargetedWithAccuracyAction> owAction;

	public OverwatchAction(Agent performerAgent) {
		super(performerAgent);
		this.owAction = WeaponActionFactory.defaultWeaponActionClass(performerAgent);
	}

	@Override
	public int getAPRequired() { return 1; }

	@Override
	protected void performImpl() throws IllegalActionException {
		getPerformer().overwatch(owAction);
		log(BattleEvent.overwatchEvent(getPerformer(), OverwatchHelper.getName(owAction)));
	}

	@Override
	public String name() {
		return "Overwatch";
	}

	@Override
	public String description() {
		return "Attack the first enemy that moves in range";
	}

}
