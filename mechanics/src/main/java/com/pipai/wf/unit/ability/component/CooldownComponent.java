package com.pipai.wf.unit.ability.component;

import com.pipai.wf.battle.agent.Agent;

public interface CooldownComponent extends RoundEndComponent {

	void startCooldown();

	boolean onCooldown();

	int getCooldown();

	void setCooldown(int cooldown);

	void decrementCooldown();

	@Override
	default void onRoundEnd(Agent agent) {
		decrementCooldown();
	}

}
