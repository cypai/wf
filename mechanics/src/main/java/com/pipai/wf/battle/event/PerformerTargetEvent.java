package com.pipai.wf.battle.event;

import com.pipai.wf.battle.agent.Agent;

public abstract class PerformerTargetEvent extends BattleEvent {

	public final Agent performer;
	public final Agent target;

	public PerformerTargetEvent(Agent performer, Agent target) {
		this.performer = performer;
		this.target = target;
	}

}
