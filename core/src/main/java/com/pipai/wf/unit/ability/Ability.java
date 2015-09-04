package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.spell.Spell;

public abstract class Ability {

	private int cooldown;
	private int level;

	public Ability(int level) {
		this.level = level;
	}

	public abstract String name();

	public abstract String description();

	@Override
	public abstract Ability clone();

	public void startCooldown() {}

	public boolean isOnCooldown() {
		return cooldown > 0;
	}

	public int getCooldownTimer() {
		return 0;
	}

	public final void onRoundEnd(Agent a) {
		if (cooldown > 0) {
			cooldown -= 1;
		}
		onRoundEndImpl(a);
	}

	protected void onRoundEndImpl(Agent a) {}

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
