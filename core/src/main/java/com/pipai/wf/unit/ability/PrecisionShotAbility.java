package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.PrecisionShotAction;
import com.pipai.wf.battle.action.TargetedWithAccuracyAction;
import com.pipai.wf.battle.agent.Agent;

public class PrecisionShotAbility extends ActiveSkillTargetedAccAbility {

	public PrecisionShotAbility() {
		super(0);
	}

	@Override
	public String getName() {
		return "Precision Shot";
	}

	@Override
	public String getDescription() {
		return "Fires a shot that has +1 extra damage and +30% critical chance.";
	}

	@Override
	public Ability clone() {
		return new PrecisionShotAbility();
	}

	@Override
	public TargetedWithAccuracyAction getAction(BattleController controller, Agent performer, Agent target) {
		return new PrecisionShotAction(controller, performer, target);
	}

}
