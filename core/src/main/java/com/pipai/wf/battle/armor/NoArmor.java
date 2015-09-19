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
	public String name() {
		return "";
	}

	@Override
	public String description() {
		return "";
	}

}
