package com.pipai.wf.battle.effect;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.misc.Copyable;

public abstract class StatusEffect implements Copyable {

	// TODO: Remove agent reference somehow, maybe passing as argument?
	private Agent agent;
	private int cooldown;

	public StatusEffect(Agent agent, int cooldown) {
		this.agent = agent;
		this.cooldown = cooldown;
	}

	public abstract int flatAimModifier();

	public abstract int flatMobilityModifier();

	protected Agent getAgent() {
		return agent;
	}

	public abstract String name();

	public abstract String description();

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

	protected void onRoundEndImpl() {
	}

	public final void onAction(Action action) {
		onActionImpl(action);
	}

	protected void onActionImpl(Action action) {
	}

}
