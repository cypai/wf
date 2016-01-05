package com.pipai.wf.test;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentFactory;
import com.pipai.wf.battle.map.GridPosition;

public class WfTestUtils {

	public static Agent createGenericAgent(Team team, GridPosition position) {
		return new AgentFactory().battleAgentFromStats(team, position, 3, 5, 2, 65, 5, 0);
	}

}
