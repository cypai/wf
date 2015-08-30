package com.pipai.wf.battle.misc;

import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.action.TargetedWithAccuracyActionOWCapable;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;

public class OverwatchHelper {

	public static String getName(OverwatchAction owAction) {
		Agent owAgent = owAction.getPerformer();
		Agent dummy = new Agent(null, new AgentState());
		TargetedWithAccuracyActionOWCapable dummyAction = generateAction(owAction.getOWClass(), owAgent, dummy);
		return dummyAction.name();
	}

	public static TargetedWithAccuracyActionOWCapable generateAction(Class<? extends TargetedWithAccuracyActionOWCapable> actionClass, Agent performer, Agent target) {
		try {
			TargetedWithAccuracyActionOWCapable action = (actionClass.getDeclaredConstructor(Agent.class, Agent.class).newInstance(performer, target));
			return action;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
