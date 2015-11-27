package com.pipai.wf.battle.armor;

public class NoArmor extends Armor {

	public static final int MAX_HP = 0;

	public NoArmor() {
		super(MAX_HP);
	}

	@Override
	public int maxHP() {
		return MAX_HP;
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public String getDescription() {
		return "";
	}

}
