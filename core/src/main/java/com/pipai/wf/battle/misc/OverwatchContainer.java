package com.pipai.wf.battle.misc;

import com.pipai.wf.battle.action.TargetedWithAccuracyAction;
import com.pipai.wf.battle.agent.Agent;

public class OverwatchContainer {
	
	private Class<? extends TargetedWithAccuracyAction> actionClass;
	
	public void prepareAction(Class<? extends TargetedWithAccuracyAction> a) {
		actionClass = a;
	}
	
	public TargetedWithAccuracyAction generateAction(Agent performer, Agent target) {
		return OverwatchHelper.generateAction(actionClass, performer, target);
	}
	
	public boolean isEmpty() {
		return actionClass == null;
	}
	
	public void clear() {
		actionClass = null;
	}
	
}
