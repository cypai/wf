package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.AccuracyPercentages;

public abstract class TargetedWithAccuracyAction extends TargetedAction implements AccuracyPercentages {

	public TargetedWithAccuracyAction(Agent performerAgent, Agent targetAgent) {
		super(performerAgent, targetAgent);
	}

}
