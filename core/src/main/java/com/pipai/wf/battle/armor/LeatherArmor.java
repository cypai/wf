package com.pipai.wf.battle.armor;

public class LeatherArmor extends Armor {

	@Override
	public int maxHP() {
		return 3;
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
