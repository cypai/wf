package com.pipai.wf.battle.damage;

public class PercentageModifier {

	private final String modifierName;
	private final int modifier;

	public PercentageModifier(String modifierName, int modifier) {
		this.modifierName = modifierName;
		this.modifier = modifier;
	}

	public int getModifier() {
		return modifier;
	}

	public String getModifierName() {
		return modifierName;
	}

}
