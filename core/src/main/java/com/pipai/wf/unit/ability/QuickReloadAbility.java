package com.pipai.wf.unit.ability;

public class QuickReloadAbility extends Ability {

	@Override
	public String getName() {
		return "Quick Reload";
	}

	@Override
	public String getDescription() {
		return "Reloading no longer ends the turn";
	}

	@Override
	public QuickReloadAbility copy() {
		return new QuickReloadAbility();
	}

	@Override
	public QuickReloadAbility copyAsNew() {
		return new QuickReloadAbility();
	}

}
