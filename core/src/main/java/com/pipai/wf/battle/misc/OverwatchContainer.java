package com.pipai.wf.battle.misc;

import com.pipai.wf.battle.action.TargetedWithAccuracyActionOWCapable;
import com.pipai.wf.battle.agent.Agent;

public class OverwatchContainer {

	private Class<? extends TargetedWithAccuracyActionOWCapable> actionClass;

	public void prepareAction(Class<? extends TargetedWithAccuracyActionOWCapable> a) {
		actionClass = a;
	}

	public TargetedWithAccuracyActionOWCapable generateAction(Agent performer, Agent target) {
		return OverwatchHelper.generateAction(actionClass, performer, target);
	}

	public boolean isEmpty() {
		return actionClass == null;
	}

	public void clear() {
		actionClass = null;
	}

}
