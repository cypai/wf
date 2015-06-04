package com.pipai.wf.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.exception.BadStateStringException;

public class AgentLineOfSightTest {

	@Test
	public void testPeekFlankLOS() {
		/*
		 * Map looks like:
		 * A 1 0 0
		 * 0 0 0 0
		 * 0 1 0 0
		 * 0 E 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 1 3\n"
				+ "s 1 1";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(0, 3);
		GridPosition enemyPos = new GridPosition(1, 0);
		map.addAgent(AgentState.battleAgentFromStats(Team.PLAYER, playerPos, AgentState.statsOnlyState(1, 1, 1, 1, 1, 1)));
		map.addAgent(AgentState.battleAgentFromStats(Team.ENEMY, enemyPos, AgentState.statsOnlyState(1, 1, 1, 1, 1, 1)));
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertTrue(player.canSee(enemy));
		assertTrue(enemy.canSee(player));
	}
	
}
