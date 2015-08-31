package com.pipai.wf.unit.ability;

public class QuickReloadAbility extends PassiveAbility {

	public QuickReloadAbility() {
		super(AbilityType.QUICK_RELOAD, 0);
	}

	@Override
	public String name() {
		return "Quick Reload";
	}

	@Override
	public String description() {
		return "Reloading no longer ends the turn";
	}

	@Override
	public Ability clone() {
		return new QuickReloadAbility();
	}

}
