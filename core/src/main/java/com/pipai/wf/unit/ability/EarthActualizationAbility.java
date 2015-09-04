package com.pipai.wf.unit.ability;

public class EarthActualizationAbility extends PassiveAbility {

	public EarthActualizationAbility(int level) {
		super(AbilityType.EARTH_ACTUALIZATION, level);
	}

	@Override
	public String name() {
		return "Earth Actualization";
	}

	@Override
	public String description() {
		return "Allows casting of earth spells";
	}

	@Override
	public Ability clone() {
		return new EarthActualizationAbility(getLevel());
	}

}
