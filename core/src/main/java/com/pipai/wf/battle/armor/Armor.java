package com.pipai.wf.battle.armor;

public abstract class Armor {
	
	protected int hp;
	
	public Armor() {
		hp = maxHP();
	}
	
	public int getHP() {
		return hp;
	}
	
	public void decrementHP(int val) {
		hp -= val;
	}
	
	public abstract int maxHP();
	
}
