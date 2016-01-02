package com.pipai.wf.unit.ability;

public class ArrowRainAbility extends Ability {

	@Override
	public String getName() {
		return "Arrow Rain";
	}

	@Override
	public String getDescription() {
		return "Primary attacks with the bow no longer end the turn if taken as the first action";
	}

	@Override
	public Ability clone() {
		return new ArrowRainAbility();
	}

}
