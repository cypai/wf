package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.action.TargetedWithAccuracyAction;
import com.pipai.wf.battle.agent.Agent;

public abstract class ActiveSkillTargetedAccAbility extends ActiveSkillTargetedAbility {

	public ActiveSkillTargetedAccAbility(int level) {
		super(level);
	}

	@Override
	public abstract TargetedWithAccuracyAction getAction(Agent performer, Agent target);

}
