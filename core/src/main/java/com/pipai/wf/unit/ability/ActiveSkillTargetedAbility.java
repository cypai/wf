package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.TargetedAction;
import com.pipai.wf.battle.agent.Agent;

public abstract class ActiveSkillTargetedAbility extends ActiveSkillAbility {

	public ActiveSkillTargetedAbility(int level) {
		super(level);
	}

	public abstract TargetedAction getAction(BattleController controller, Agent performer, Agent target);

}
