package com.pipai.wf.battle.event;

import com.pipai.wf.battle.agent.Agent;

public class AgentTextBattleEvent extends BattleEvent {

	public final Agent performer;
	public final String text;

	public AgentTextBattleEvent(Agent performer, String text) {
		this.performer = performer;
		this.text = text;
	}

	@Override
	public String toString() {
		return performer.getName() + " performs " + text;
	}

}
