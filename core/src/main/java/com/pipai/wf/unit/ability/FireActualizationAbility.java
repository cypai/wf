package com.pipai.wf.unit.ability;

public class FireActualizationAbility extends PassiveAbility {

	public FireActualizationAbility(int level) {
		super(level);
	}

	@Override
	public String name() {
		return "Fire Actualization";
	}

	@Override
	public String description() {
		return "Allows casting of fire spells";
	}

	@Override
	public Ability clone() {
		return new FireActualizationAbility(getLevel());
	}

}
