package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.PrecisionShotAction;
import com.pipai.wf.battle.action.TargetedWithAccuracyAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.unit.ability.component.CooldownImpl;
import com.pipai.wf.unit.ability.component.HasCooldownComponent;
import com.pipai.wf.unit.ability.component.TargetedAccAbilityComponent;

public class PrecisionShotAbility extends Ability implements TargetedAccAbilityComponent, HasCooldownComponent {

	private CooldownImpl cooldownImpl = new CooldownImpl(1);

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
	public CooldownImpl getCooldownImpl() {
		return cooldownImpl;
	}

	@Override
	public TargetedWithAccuracyAction getTargetedAccAction(BattleController controller, Agent performer, Agent target) {
		return new PrecisionShotAction(controller, performer, target);
	}

}
