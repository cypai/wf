package com.pipai.wf.test;

import static org.junit.Assert.*;

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

	@Test
	public void testDiagonalTopOpenCoverLOS() {
		/*
		 * Map looks like:
		 * 0 0 0 E
		 * 0 1 0 0
		 * 0 0 0 0
		 * A 0 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 1 2";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(3, 3);
		map.addAgent(AgentState.battleAgentFromStats(Team.PLAYER, playerPos, AgentState.statsOnlyState(1, 1, 1, 1, 1, 1)));
		map.addAgent(AgentState.battleAgentFromStats(Team.ENEMY, enemyPos, AgentState.statsOnlyState(1, 1, 1, 1, 1, 1)));
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertTrue(player.canSee(enemy));
		assertTrue(enemy.canSee(player));
	}

	@Test
	public void testDiagonalBottomOpenCoverLOS() {
		/*
		 * Map looks like:
		 * 0 0 0 E
		 * 0 0 0 0
		 * 0 0 1 0
		 * A 0 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 2 1";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(3, 3);
		map.addAgent(AgentState.battleAgentFromStats(Team.PLAYER, playerPos, AgentState.statsOnlyState(1, 1, 1, 1, 1, 1)));
		map.addAgent(AgentState.battleAgentFromStats(Team.ENEMY, enemyPos, AgentState.statsOnlyState(1, 1, 1, 1, 1, 1)));
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertTrue(player.canSee(enemy));
		assertTrue(enemy.canSee(player));
	}

	@Test
	public void testDiagonalBlockedCoverLOS() {
		/*
		 * Map looks like:
		 * 0 0 0 E
		 * 0 1 0 0
		 * 0 0 1 0
		 * A 0 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 1 2\n"
				+ "s 2 1";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(3, 3);
		map.addAgent(AgentState.battleAgentFromStats(Team.PLAYER, playerPos, AgentState.statsOnlyState(1, 1, 1, 1, 1, 1)));
		map.addAgent(AgentState.battleAgentFromStats(Team.ENEMY, enemyPos, AgentState.statsOnlyState(1, 1, 1, 1, 1, 1)));
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertFalse(player.canSee(enemy));
		assertFalse(enemy.canSee(player));
	}

	@Test
	public void testSteepDiagonalBlockedCoverLOS() {
		/*
		 * Map looks like:
		 * 0 0 E 0
		 * 0 1 0 0
		 * 0 0 1 0
		 * 0 A 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 1 2\n"
				+ "s 2 1";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 0);
		GridPosition enemyPos = new GridPosition(2, 3);
		map.addAgent(AgentState.battleAgentFromStats(Team.PLAYER, playerPos, AgentState.statsOnlyState(1, 1, 1, 1, 1, 1)));
		map.addAgent(AgentState.battleAgentFromStats(Team.ENEMY, enemyPos, AgentState.statsOnlyState(1, 1, 1, 1, 1, 1)));
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertFalse(player.canSee(enemy));
		assertFalse(enemy.canSee(player));
	}

	@Test
	public void testFlatDiagonalBlockedCoverLOS() {
		/*
		 * Map looks like:
		 * 0 0 0 0
		 * 0 1 0 E
		 * A 0 1 0
		 * 0 0 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 1 2\n"
				+ "s 2 1";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(0, 1);
		GridPosition enemyPos = new GridPosition(3, 2);
		map.addAgent(AgentState.battleAgentFromStats(Team.PLAYER, playerPos, AgentState.statsOnlyState(1, 1, 1, 1, 1, 1)));
		map.addAgent(AgentState.battleAgentFromStats(Team.ENEMY, enemyPos, AgentState.statsOnlyState(1, 1, 1, 1, 1, 1)));
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertFalse(player.canSee(enemy));
		assertFalse(enemy.canSee(player));
	}

	@Test
	public void testOpenLOS() {
		BattleMap map = new BattleMap(1, Agent.RANGE + 1);
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(Agent.RANGE - 1, 0);
		map.addAgent(AgentState.battleAgentFromStats(Team.PLAYER, playerPos, AgentState.statsOnlyState(1, 1, 1, 1, 1, 1)));
		map.addAgent(AgentState.battleAgentFromStats(Team.ENEMY, enemyPos, AgentState.statsOnlyState(1, 1, 1, 1, 1, 1)));
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertTrue(player.canSee(enemy));
		assertTrue(enemy.canSee(player));
	}

	@Test
	public void testTooFarLOS() {
		BattleMap map = new BattleMap(1, Agent.RANGE + 1);
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(Agent.RANGE, 0);
		map.addAgent(AgentState.battleAgentFromStats(Team.PLAYER, playerPos, AgentState.statsOnlyState(1, 1, 1, 1, 1, 1)));
		map.addAgent(AgentState.battleAgentFromStats(Team.ENEMY, enemyPos, AgentState.statsOnlyState(1, 1, 1, 1, 1, 1)));
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertFalse(player.canSee(enemy));
		assertFalse(enemy.canSee(player));
	}
	
}
