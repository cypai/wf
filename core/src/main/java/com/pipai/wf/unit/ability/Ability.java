package com.pipai.wf.unit.ability;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.exception.NoRegisteredAgentException;
import com.pipai.wf.misc.HasDescription;
import com.pipai.wf.misc.HasName;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class Ability implements HasName, HasDescription {

	private int cooldown;
	private int level;
	private Agent agent;

	@JsonCreator
	public Ability(@JsonProperty("level") int level) {
		this.level = level;
	}

	@Override
	public abstract Ability clone();

	public void registerToAgent(Agent agent) {
		this.agent = agent;
	}

	public Agent getAgent() {
		return agent;
	}

	public void startCooldown() {
	}

	public boolean onCooldown() {
		return cooldown > 0;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public void decrementCooldown() {
		if (cooldown > 0) {
			cooldown--;
		}
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

	protected void onRoundEndImpl() {
	}

	public boolean grantsSpell() {
		return false;
	}

	public Spell grantedSpell() {
		return null;
	}

	public final int getLevel() {
		return level;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Ability) {
			if (!this.getClass().isInstance(other)) {
				return false;
			}
			return (getLevel() == ((Ability) other).getLevel());
		} else {
			return false;
		}
	}

	public boolean equalsIgnoreLevel(Ability other) {
		return this.getClass().isInstance(other);
	}

}
