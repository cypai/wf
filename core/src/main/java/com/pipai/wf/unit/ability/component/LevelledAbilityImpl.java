package com.pipai.wf.unit.ability.component;

public class LevelledAbilityImpl implements LevelledAbilityComponent {

	private int level;

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public void setLevel(int level) {
		this.level = level;
	}

}
