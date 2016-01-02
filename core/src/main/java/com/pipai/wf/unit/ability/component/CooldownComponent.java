package com.pipai.wf.unit.ability.component;

import com.pipai.wf.battle.agent.Agent;

public interface CooldownComponent extends RoundEndComponent {

	void startCooldown();

	public boolean onCooldown();

	public int getCooldown();

	public void setCooldown(int cooldown);

	public void decrementCooldown();

	@Override
	default void onRoundEnd(Agent agent) {
		decrementCooldown();
	}

}
