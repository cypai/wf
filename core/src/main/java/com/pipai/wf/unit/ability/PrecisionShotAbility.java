package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.PrecisionShotAction;
import com.pipai.wf.battle.action.TargetedAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.unit.ability.component.CooldownComponent;
import com.pipai.wf.unit.ability.component.CooldownComponentImpl;
import com.pipai.wf.unit.ability.component.HasCooldownComponent;
import com.pipai.wf.unit.ability.component.TargetedAbilityComponent;

public class PrecisionShotAbility extends Ability implements TargetedAbilityComponent, HasCooldownComponent {

	private CooldownComponent cooldownImpl = new CooldownComponentImpl(1);

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
	public CooldownComponent getCooldownComponent() {
		return cooldownImpl;
	}

	@Override
	public TargetedAction getTargetedAction(BattleController controller, Agent performer, Agent target) {
		return new PrecisionShotAction(controller, performer, target);
	}

}
