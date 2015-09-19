package com.pipai.wf.battle.armor;

public class LeatherArmor extends Armor {

	public static final int MAX_HP = 3;

	public LeatherArmor() {
		super(MAX_HP);
	}

	@Override
	public int maxHP() {
		return MAX_HP;
	}

	@Override
	public String name() {
		return "Leather Armor";
	}

	@Override
	public String description() {
		return "Simple leather armor.";
	}

}
