package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.AccuracyPercentages;
import com.pipai.wf.battle.damage.PercentageModifierList;

public abstract class TargetedWithAccuracyAction extends TargetedAction implements AccuracyPercentages {

	public TargetedWithAccuracyAction(BattleController controller, Agent performerAgent, Agent targetAgent) {
		super(controller, performerAgent, targetAgent);
	}

	public TargetedWithAccuracyAction(BattleController controller, Agent performerAgent) {
		super(controller, performerAgent);
	}

	public abstract PercentageModifierList getHitCalculation();

	public abstract PercentageModifierList getCritCalculation();

}
