package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.agent.Agent;

public class RegenerationAbility extends PassiveAbility {

	public RegenerationAbility(int level) {
		super(AbilityType.REGENERATION, level);
	}

	@Override
	public String name() {
		return "Regeneration";
	}

	@Override
	public String description() {
		return "Regenerates a set amount of HP per turn";
	}

	@Override
	public Ability clone() {
		return new RegenerationAbility(getLevel());
	}

	@Override
	protected void onRoundEnd(Agent a) {
		a.heal(super.getLevel());
	}

}
