package com.pipai.wf.battle.overwatch;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.TargetedWithAccuracyActionOWCapable;
import com.pipai.wf.battle.agent.Agent;

public class OverwatchActivatedActionSchema {

	private Class<? extends TargetedWithAccuracyActionOWCapable> actionClass;

	public OverwatchActivatedActionSchema(Class<? extends TargetedWithAccuracyActionOWCapable> a) {
		actionClass = a;
	}

	public TargetedWithAccuracyActionOWCapable build(BattleController controller, Agent performer, Agent target) {
		return OverwatchHelper.generateAction(this, controller, performer, target);
	}

	protected Class<? extends TargetedWithAccuracyActionOWCapable> getActionClass() {
		return actionClass;
	}

}
