package com.pipai.wf.battle.misc;

import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.action.TargetedWithAccuracyAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;

public class OverwatchHelper {

	public static String getName(OverwatchAction owAction) {
		Agent owAgent = owAction.getPerformer();
		Agent dummy = new Agent(null, new AgentState());
		TargetedWithAccuracyAction dummyAction = generateAction(owAction.getOWClass(), owAgent, dummy);
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
