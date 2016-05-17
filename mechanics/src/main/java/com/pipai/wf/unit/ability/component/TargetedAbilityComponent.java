package com.pipai.wf.unit.ability.component;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.TargetedAction;
import com.pipai.wf.battle.agent.Agent;

public interface TargetedAbilityComponent {

	TargetedAction getTargetedAction(BattleController controller, Agent performer, Agent target);

}
