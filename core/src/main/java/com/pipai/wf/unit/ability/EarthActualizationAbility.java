package com.pipai.wf.unit.ability;

public class EarthActualizationAbility extends PassiveAbility {

	public EarthActualizationAbility(int level) {
		super(level);
	}

	@Override
	public String getName() {
		return "Earth Actualization";
	}

	@Override
	public String getDescription() {
		return "Allows casting of earth spells";
	}

	@Override
	public Ability clone() {
		return new EarthActualizationAbility(getLevel());
	}

}
