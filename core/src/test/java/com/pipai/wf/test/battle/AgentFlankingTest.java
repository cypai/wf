package com.pipai.wf.test.battle;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.CoverType;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.exception.BadStateStringException;
import com.pipai.wf.test.WfConfiguredTest;

public class AgentFlankingTest extends WfConfiguredTest {

	@Test
	public void testVerticalFlank() {
		/*
		 * Map looks like:
		 * 0 0 0 0
		 * 0 A 1 0
		 * 0 0 0 0
		 * 0 E 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 2 2";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 2);
		GridPosition enemyPos = new GridPosition(1, 0);
		map.addAgent(AgentStateFactory.battleAgentFromStats(Team.PLAYER, playerPos, AgentStateFactory.statsOnlyState(1, 1, 1, 1, 1, 1)));
		map.addAgent(AgentStateFactory.battleAgentFromStats(Team.ENEMY, enemyPos, AgentStateFactory.statsOnlyState(1, 1, 1, 1, 1, 1)));
		Agent player = map.getAgentAtPos(playerPos);
		assertTrue(player.getCoverType() == CoverType.FULL);
		assertTrue(player.isFlanked());
	}

	@Test
	public void testVerticalNonFlank() {
		/*
		 * Map looks like:
		 * 0 0 0 0
		 * 0 A 0 0
		 * 0 1 0 0
		 * 0 E 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 1 1";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 2);
		GridPosition enemyPos = new GridPosition(1, 0);
		map.addAgent(AgentStateFactory.battleAgentFromStats(Team.PLAYER, playerPos, AgentStateFactory.statsOnlyState(1, 1, 1, 1, 1, 1)));
		map.addAgent(AgentStateFactory.battleAgentFromStats(Team.ENEMY, enemyPos, AgentStateFactory.statsOnlyState(1, 1, 1, 1, 1, 1)));
		Agent player = map.getAgentAtPos(playerPos);
		assertTrue(player.getCoverType() == CoverType.FULL);
		assertFalse(player.isFlanked());
	}

	@Test
	public void testPeekFlank() {
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
		map.addAgent(AgentStateFactory.battleAgentFromStats(Team.PLAYER, playerPos, AgentStateFactory.statsOnlyState(1, 1, 1, 1, 1, 1)));
		map.addAgent(AgentStateFactory.battleAgentFromStats(Team.ENEMY, enemyPos, AgentStateFactory.statsOnlyState(1, 1, 1, 1, 1, 1)));
		Agent player = map.getAgentAtPos(playerPos);
		assertTrue(player.getCoverType() == CoverType.FULL);
		assertTrue(player.isFlanked());
	}

	@Test
	public void testClosePeekFlank() {
		/*
		 * Map looks like:
		 * A 1 0 0
		 * 0 E 0 0
		 * 0 0 0 0
		 * 0 0 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 1 3";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(0, 3);
		GridPosition enemyPos = new GridPosition(1, 2);
		map.addAgent(AgentStateFactory.battleAgentFromStats(Team.PLAYER, playerPos, AgentStateFactory.statsOnlyState(1, 1, 1, 1, 1, 1)));
		map.addAgent(AgentStateFactory.battleAgentFromStats(Team.ENEMY, enemyPos, AgentStateFactory.statsOnlyState(1, 1, 1, 1, 1, 1)));
		Agent player = map.getAgentAtPos(playerPos);
		assertTrue(player.getCoverType() == CoverType.FULL);
		assertTrue(player.isFlanked());
	}
	
}
