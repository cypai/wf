package com.pipai.wf.unit.race;

import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;

public enum Race {
	
	HUMAN(AgentStateFactory.statsOnlyState(7, 5, 2, 12, 65, 0)),
	FAIRY(AgentStateFactory.statsOnlyState(4, 10, 2, 13, 65, 0)),
	CAT(AgentStateFactory.statsOnlyState(4, 3, 3, 14, 65, 5)),
	FOX(AgentStateFactory.statsOnlyState(5, 10, 2, 13, 65, 0));
	
	private AgentState baseline;
	
	private Race(AgentState baseline) {
		this.baseline = baseline;
	}
	
	public AgentState getBaseStats() {
		return baseline.statsOnlyCopy();
	}
	
}
