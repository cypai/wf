package com.pipai.wf.battle.map;

public abstract class EnvironmentObject {

	private int hp;	// For calculating cover destruction

	public EnvironmentObject(int maxHP) {
		hp = maxHP;
	}

	public void takeDamage(int dmg) {
		hp -= dmg;
	}

	public int getHP() {
		return hp;
	}

	public abstract CoverType getCoverType();

}
