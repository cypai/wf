package com.pipai.wf.unit.ability;

import com.pipai.wf.unit.ability.component.HasLevelledAbilityComponent;
import com.pipai.wf.unit.ability.component.LevelledAbilityComponent;
import com.pipai.wf.unit.ability.component.LevelledAbilityImpl;

public class FireActualizationAbility extends Ability implements HasLevelledAbilityComponent {

	private LevelledAbilityComponent levelledAbilityComponent = new LevelledAbilityImpl();

	public FireActualizationAbility() {
		setLevel(1);
	}

	public FireActualizationAbility(int level) {
		setLevel(level);
	}

	@Override
	public LevelledAbilityComponent getLevelledAbilityComponent() {
		return levelledAbilityComponent;
	}

	@Override
	public String getName() {
		return "Fire Actualization";
	}

	@Override
	public String getDescription() {
		return "Allows casting of fire spells";
	}

	@Override
	public Ability clone() {
		return new FireActualizationAbility(getLevel());
	}

}
