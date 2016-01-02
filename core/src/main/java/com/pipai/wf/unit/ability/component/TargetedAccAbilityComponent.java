package com.pipai.wf.unit.ability.component;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.TargetedAction;
import com.pipai.wf.battle.action.TargetedWithAccuracyAction;
import com.pipai.wf.battle.agent.Agent;

public interface TargetedAccAbilityComponent extends TargetedAbilityComponent {

	TargetedWithAccuracyAction getTargetedAccAction(BattleController controller, Agent performer, Agent target);

	@Override
	default TargetedAction getTargetedAction(BattleController controller, Agent performer, Agent target) {
		return getTargetedAccAction(controller, performer, target);
	}

}
