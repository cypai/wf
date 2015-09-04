package com.pipai.wf.unit.ability;

public class ElectricActualizationAbility extends PassiveAbility {

	public ElectricActualizationAbility(int level) {
		super(AbilityType.ELECTRIC_ACTUALIZATION, level);
	}

	@Override
	public String name() {
		return "Electric Actualization";
	}

	@Override
	public String description() {
		return "Allows casting of electric spells";
	}

	@Override
	public Ability clone() {
		return new ElectricActualizationAbility(getLevel());
	}

}
