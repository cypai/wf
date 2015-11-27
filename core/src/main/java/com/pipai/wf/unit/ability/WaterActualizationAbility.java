package com.pipai.wf.unit.ability;

public class WaterActualizationAbility extends PassiveAbility {

	public WaterActualizationAbility(int level) {
		super(level);
	}

	@Override
	public String getName() {
		return "Water Actualization";
	}

	@Override
	public String getDescription() {
		return "Allows casting of water spells";
	}

	@Override
	public Ability clone() {
		return new WaterActualizationAbility(getLevel());
	}

}
