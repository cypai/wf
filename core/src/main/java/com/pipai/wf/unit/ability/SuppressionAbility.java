package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.SuppressionAction;
import com.pipai.wf.battle.action.TargetedAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.unit.ability.component.TargetedAbilityComponent;

public class SuppressionAbility extends Ability implements TargetedAbilityComponent {

	@Override
	public TargetedAction getTargetedAction(BattleController controller, Agent performer, Agent target) {
		return new SuppressionAction(controller, performer, target);
	}

	@Override
	public String getName() {
		return "Suppression";
	}

	@Override
	public String getDescription() {
		return "Decreases the target's aim, crit chance, and AOE range";
	}

	@Override
	public SuppressionAbility copy() {
		return new SuppressionAbility();
	}

	@Override
	public SuppressionAbility copyAsNew() {
		return new SuppressionAbility();
	}

}
