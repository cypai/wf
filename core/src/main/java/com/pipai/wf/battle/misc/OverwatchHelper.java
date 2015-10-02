package com.pipai.wf.battle.misc;

import java.lang.reflect.InvocationTargetException;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.action.TargetedWithAccuracyActionOWCapable;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;

public class OverwatchHelper {

	public static String getName(OverwatchAction owAction) {
		Agent owAgent = owAction.getPerformer();
		Agent dummy = new Agent(new AgentState(new BattleConfiguration()), null);
		TargetedWithAccuracyActionOWCapable dummyAction = generateAction(owAction.getOWClass(), owAgent, dummy);
		return dummyAction.name();
	}

	public static TargetedWithAccuracyActionOWCapable generateAction(Class<? extends TargetedWithAccuracyActionOWCapable> actionClass, Agent performer, Agent target) {
		TargetedWithAccuracyActionOWCapable action;
		try {
			action = (actionClass.getDeclaredConstructor(Agent.class, Agent.class).newInstance(performer, target));
			return action;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}

}
