package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.AccuracyPercentages;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.exception.IllegalActionException;

public abstract class TargetedWithAccuracyAction extends TargetedAction implements AccuracyPercentages {

	public TargetedWithAccuracyAction(Agent performerAgent, Agent targetAgent) {
		super(performerAgent, targetAgent);
	}
	
	public abstract void performOnOverwatch(BattleEvent parent) throws IllegalActionException;

}
