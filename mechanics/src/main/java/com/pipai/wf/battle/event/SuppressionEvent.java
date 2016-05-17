package com.pipai.wf.battle.event;

import com.pipai.wf.battle.agent.Agent;

public class SuppressionEvent extends PerformerTargetEvent {

	public SuppressionEvent(Agent performer, Agent target) {
		super(performer, target);
	}

	@Override
	public String toString() {
		return performer.getName() + " suppresses " + target.getName();
	}

}
