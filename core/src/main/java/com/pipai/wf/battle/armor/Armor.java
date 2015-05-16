package com.pipai.wf.battle.armor;

public abstract class Armor {
	
	protected int hp;
	
	public Armor() {
		hp = maxHP();
	}
	
	public int getHP() {
		return hp;
	}
	
	/**
	 * @return Amount of damage that went through the armor
	 */
	public int takeDamage(int val) {
		if (val > hp) {
			int pierceDmg = val - hp;
			hp = 0;
			return pierceDmg;
		} else {
			hp -= val;
			return 0;
		}
	}
	
	public abstract int maxHP();
	
}
