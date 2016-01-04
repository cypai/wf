package com.pipai.wf.item.armor;

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
	public String getName() {
		return "Leather Armor";
	}

	@Override
	public String getDescription() {
		return "Simple leather armor.";
	}

}
