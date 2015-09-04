package com.pipai.wf.unit.ability;

public class WaterActualizationAbility extends PassiveAbility {

	public WaterActualizationAbility(int level) {
		super(level);
	}

	@Override
	public String name() {
		return "Water Actualization";
	}

	@Override
	public String description() {
		return "Allows casting of water spells";
	}

	@Override
	public Ability clone() {
		return new WaterActualizationAbility(getLevel());
	}

}
