package com.pipai.wf.unit.ability.component;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface HasLevelledAbilityComponent extends LevelledAbilityComponent {

	@JsonIgnore
	LevelledAbilityComponent getLevelledAbilityComponent();

	@Override
	default int getLevel() {
		return getLevelledAbilityComponent().getLevel();
	}

	@Override
	default void setLevel(int level) {
		getLevelledAbilityComponent().setLevel(level);
	}

}
