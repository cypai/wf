package com.pipai.wf.unit.ability;

public class ElectricActualizationAbility extends PassiveAbility {

	public ElectricActualizationAbility(int level) {
		super(level);
	}

	@Override
	public String getName() {
		return "Electric Actualization";
	}

	@Override
	public String getDescription() {
		return "Allows casting of electric spells";
	}

	@Override
	public Ability clone() {
		return new ElectricActualizationAbility(getLevel());
	}

}
