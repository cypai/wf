package com.pipai.wf.unit.ability;

public class LightActualizationAbility extends PassiveAbility {

	public LightActualizationAbility(int level) {
		super(AbilityType.LIGHT_ACTUALIZATION, level);
	}

	@Override
	public String name() {
		return "Light Actualization";
	}

	@Override
	public String description() {
		return "Allows casting of light spells";
	}

	@Override
	public Ability clone() {
		return new LightActualizationAbility(getLevel());
	}

}
