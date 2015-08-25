package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.agent.Agent;

public class RegenerationAbility extends Ability {
	
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
	public void onRoundEnd(Agent a) {
		a.heal(super.getLevel());
	}
	
}
