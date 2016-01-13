package com.pipai.wf.battle.event;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.GridPosition;

public class OverwatchActivationEvent extends PerformerTargetEvent {

	public final GridPosition location;

	public OverwatchActivationEvent(Agent performer, Agent target, GridPosition location, BattleEvent activatedEvent) {
		super(performer, target);
		this.location = location;
		addChainEvent(activatedEvent);
	}

	@Override
	public String toString() {
		return target.getName() + " activated overwatch from " + performer.getName();
	}

}
