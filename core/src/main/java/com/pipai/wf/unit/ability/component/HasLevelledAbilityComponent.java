package com.pipai.wf.unit.ability.component;

public interface HasLevelledAbilityComponent extends LevelledAbilityComponent {

	LevelledAbilityImpl getLevelledAbilityImpl();

	@Override
	default int getLevel() {
		return getLevelledAbilityImpl().getLevel();
	}

	@Override
	default void setLevel(int level) {
		getLevelledAbilityImpl().setLevel(level);
	}

}
