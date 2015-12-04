package com.pipai.wf.battle.overwatch;

import java.lang.reflect.InvocationTargetException;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.action.TargetedWithAccuracyActionOWCapable;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.misc.BasicStats;

public class OverwatchHelper {

	public static String getName(OverwatchAction owAction) {
		Agent owAgent = owAction.getPerformer();
		Agent dummy = new Agent(new AgentState(null, new BasicStats(1, 1, 1, 1, 1, 1, 1, 1, 1)));
		TargetedWithAccuracyActionOWCapable dummyAction = generateAction(owAction.getOverwatchActionSchema(), null, owAgent, dummy);
		return dummyAction.getName();
	}

	public static TargetedWithAccuracyActionOWCapable generateAction(OverwatchActivatedActionSchema schema, BattleController controller, Agent performer, Agent target) {
		TargetedWithAccuracyActionOWCapable action;
		try {
			action = schema.getActionClass().getDeclaredConstructor(BattleController.class, Agent.class, Agent.class).newInstance(controller, performer, target);
			return action;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException("Could not generate the overwatch activation action for class: " + schema.getActionClass(), e);
		}
	}

}
