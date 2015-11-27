package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.exception.IllegalActionException;

public abstract class TargetedWithAccuracyActionOWCapable extends TargetedWithAccuracyAction {

	public TargetedWithAccuracyActionOWCapable(BattleController controller, Agent performerAgent, Agent targetAgent) {
		super(controller, performerAgent, targetAgent);
	}

	public abstract void performOnOverwatch(BattleEvent parent) throws IllegalActionException;

}
