package com.pipai.wf.unit.ability;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FireActualizationAbility extends PassiveAbility {

	@JsonCreator
	public FireActualizationAbility(@JsonProperty("level") int level) {
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
