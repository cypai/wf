package com.pipai.wf.battle.spell;

public class FireballSpell extends Spell {

	@Override
	public int requiredMP() {
		return 1;
	}

	@Override
	public int getMinDamage() {
		return 3;
	}

	@Override
	public int getMaxDamage() {
		return 5;
	}

	@Override
	public String description() {
		return "A simple fire spell that does good damage";
	}

}
