package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.action.PrecisionShotAction;
import com.pipai.wf.battle.action.TargetedWithAccuracyAction;
import com.pipai.wf.battle.agent.Agent;

public class PrecisionShotAbility extends ActiveSkillTargetedAccAbility {

	public PrecisionShotAbility() {
		super(AbilityType.PRECISION_SHOT, 0);
	}

	@Override
	public String name() {
		return "Precision Shot";
	}

	@Override
	public String description() {
		return "Fires a shot that has +1 extra damage and +30% critical chance.";
	}

	@Override
	public TargetedWithAccuracyAction getAction(Agent performer, Agent target) {
		return new PrecisionShotAction(performer, target);
	}

}
