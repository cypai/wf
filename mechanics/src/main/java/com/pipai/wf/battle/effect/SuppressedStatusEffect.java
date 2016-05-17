package com.pipai.wf.battle.effect;

import com.pipai.wf.battle.agent.Agent;

public class SuppressedStatusEffect extends StatusEffect {

	public SuppressedStatusEffect(Agent agent) {
		super(agent, 1);
	}

	@Override
	public int flatAimModifier() {
		return -30;
	}

	@Override
	public int flatMobilityModifier() {
		return 0;
	}

	@Override
	public String name() {
		return "Suppressed";
	}

	@Override
	public String description() {
		return "-30 to aim, reduces range of AOE actions, and causes reaction fire upon movement";
	}

	@Override
	public SuppressedStatusEffect copy() {
		return new SuppressedStatusEffect(getAgent());
	}

}
