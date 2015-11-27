package com.pipai.wf.unit.ability;

public class ForceActualizationAbility extends PassiveAbility {

	public ForceActualizationAbility(int level) {
		super(level);
	}

	@Override
	public String getName() {
		return "Force Actualization";
	}

	@Override
	public String getDescription() {
		return "Allows casting of force spells";
	}

	@Override
	public Ability clone() {
		return new ForceActualizationAbility(getLevel());
	}

}
