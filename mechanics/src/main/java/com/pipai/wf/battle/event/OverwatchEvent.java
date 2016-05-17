package com.pipai.wf.battle.event;

import com.pipai.wf.battle.agent.Agent;

public class OverwatchEvent extends BattleEvent {

	public final Agent performer;
	public final String overwatchName;

	public OverwatchEvent(Agent performer, String overwatchName) {
		this.performer = performer;
		this.overwatchName = "Overwatch: " + overwatchName;
	}

	@Override
	public String toString() {
		return performer.getName() + " performs " + overwatchName;
	}

}
