package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.exception.NoRegisteredAgentException;

public abstract class Ability {

	private int cooldown;
	private int level;
	private Agent agent;

	public Ability(int level) {
		this.level = level;
	}

	public abstract String name();

	public abstract String description();

	@Override
	public abstract Ability clone();

	public void registerToAgent(Agent agent) {
		this.agent = agent;
	}

	public Agent getAgent() {
		return agent;
	}

	public void startCooldown() {}

	public boolean isOnCooldown() {
		return cooldown > 0;
	}

	public int getCooldownTimer() {
		return 0;
	}

	private void requireRegisteredAgent() throws NoRegisteredAgentException {
		if (agent == null) {
			throw new NoRegisteredAgentException();
		}
	}

	public final void onRoundEnd() throws NoRegisteredAgentException {
		requireRegisteredAgent();
		if (cooldown > 0) {
			cooldown -= 1;
		}
		onRoundEndImpl();
	}

	protected void onRoundEndImpl() {}

	public boolean grantsSpell() {
		return false;
	}

	public Spell getGrantedSpell() {
		return null;
	}

	public final int getLevel() {
		return level;
	}

	public boolean equals(Ability other) {
		if (!this.getClass().isInstance(other)) {
			return false;
		}
		return (this.getLevel() == other.getLevel());
	}

	public boolean equalsIgnoreLevel(Ability other) {
		return this.getClass().isInstance(other);
	}

}
