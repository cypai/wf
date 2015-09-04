package com.pipai.wf.battle.effect;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.agent.Agent;

public abstract class StatusEffect {

	private Agent agent;
	private int cooldown;

	public StatusEffect(Agent agent, int cooldown) {
		this.agent = agent;
		this.cooldown = cooldown;
	}

	public abstract int flatAimModifier();
	public abstract int flatMobilityModifier();

	public abstract String name();
	public abstract String description();

	public Agent getAgent() {
		return agent;
	}

	public int getCooldown() {
		return cooldown;
	}

	public final void onRoundEnd() {
		cooldown -= 1;
		if (cooldown < 0) {
			cooldown = 0;
		}
		onRoundEndImpl();
	}

	protected void onRoundEndImpl() {}

	public final void onAction(Action action) {
		onActionImpl(action);
	}

	protected void onActionImpl(Action action) {}

}
