package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.action.TargetedWithAccuracyAction;
import com.pipai.wf.battle.agent.Agent;

public abstract class ActiveSkillTargetedAccAbility extends ActiveSkillAbility {

	public ActiveSkillTargetedAccAbility(AbilityType t, int level) {
		super(t, level);
	}

	public abstract TargetedWithAccuracyAction getAction(Agent performer, Agent target);

}
