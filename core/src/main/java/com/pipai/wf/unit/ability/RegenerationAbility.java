package com.pipai.wf.unit.ability;


public class RegenerationAbility extends PassiveAbility {

	public RegenerationAbility(int level) {
		super(level);
	}

	@Override
	public String name() {
		return "Regeneration";
	}

	@Override
	public String description() {
		return "Regenerates a set amount of HP per turn";
	}

	@Override
	public Ability clone() {
		return new RegenerationAbility(getLevel());
	}

	@Override
	protected void onRoundEndImpl() {
		getAgent().heal(super.getLevel());
	}

}
