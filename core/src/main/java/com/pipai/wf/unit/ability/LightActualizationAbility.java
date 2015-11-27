package com.pipai.wf.unit.ability;

public class LightActualizationAbility extends PassiveAbility {

	public LightActualizationAbility(int level) {
		super(level);
	}

	@Override
	public String getName() {
		return "Light Actualization";
	}

	@Override
	public String getDescription() {
		return "Allows casting of light spells";
	}

	@Override
	public Ability clone() {
		return new LightActualizationAbility(getLevel());
	}

}
