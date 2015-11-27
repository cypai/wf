package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.SuppressionAction;
import com.pipai.wf.battle.action.TargetedAction;
import com.pipai.wf.battle.agent.Agent;

public class SuppressionAbility extends ActiveSkillTargetedAbility {

	public SuppressionAbility() {
		super(0);
	}

	@Override
	public TargetedAction getAction(BattleController controller, Agent performer, Agent target) {
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
	public Ability clone() {
		return new SuppressionAbility();
	}

}
