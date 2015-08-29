package com.pipai.wf.battle.misc;

import com.pipai.wf.battle.action.TargetedWithAccuracyAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;

public class OverwatchHelper {

	public static String getName(Class<? extends TargetedWithAccuracyAction> actionClass) {
		Agent dummy1 = new Agent(null, new AgentState());
		Agent dummy2 = new Agent(null, new AgentState());
		TargetedWithAccuracyAction dummyAction = generateAction(actionClass, dummy1, dummy2);
		return dummyAction.name();
	}

	public static TargetedWithAccuracyAction generateAction(Class<? extends TargetedWithAccuracyAction> actionClass, Agent performer, Agent target) {
		try {
			TargetedWithAccuracyAction action = (actionClass.getDeclaredConstructor(Agent.class, Agent.class).newInstance(performer, target));
			return action;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
