package com.pipai.wf.battle.armor;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pipai.wf.battle.item.Item;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class Armor extends Item {

	protected int hp;

	public Armor(int hp) {
		this.hp = hp;
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
