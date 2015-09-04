package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.action.TargetedWithAccuracyAction;
import com.pipai.wf.battle.agent.Agent;

public abstract class ActiveSkillTargetedAccAbility extends ActiveSkillAbility {

	public ActiveSkillTargetedAccAbility(int level) {
		super(level);
	}

	public abstract TargetedWithAccuracyAction getAction(Agent performer, Agent target);

}
