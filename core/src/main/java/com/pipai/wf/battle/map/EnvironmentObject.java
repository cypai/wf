package com.pipai.wf.battle.map;

public abstract class EnvironmentObject {
	
	protected int hp;	// For calculating cover destruction
	
	public EnvironmentObject(int maxHP) {
		hp = maxHP;
	}
	
	public void takeDamage(int dmg) {
		hp -= dmg;
	}
	
	public abstract CoverType getCoverType();
	
}
