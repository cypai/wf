package com.pipai.wf.battle.agent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.CoverType;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.exception.BadStateStringException;

public class AgentFlankingTest {

	private static BattleMap generateMap(String mapString, GridPosition playerPos, GridPosition enemyPos) throws BadStateStringException {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		when(mockConfig.sightRange()).thenReturn(17);
		BattleMap map = new BattleMap(new MapString(mapString), mock(BattleConfiguration.class));
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		map.addAgent(factory.battleAgentFromStats(Team.PLAYER, playerPos, factory.statsOnlyState(1, 1, 1, 1, 1, 0)));
		map.addAgent(factory.battleAgentFromStats(Team.ENEMY, enemyPos, factory.statsOnlyState(1, 1, 1, 1, 1, 0)));
		return map;
	}

	@Test
	public void testVerticalFlank() throws BadStateStringException {
		/*
		 * Map looks like:
		 * 0 0 0 0
		 * 0 A 1 0
		 * 0 0 0 0
		 * 0 E 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 2 2";

		GridPosition playerPos = new GridPosition(1, 2);
		GridPosition enemyPos = new GridPosition(1, 0);
		BattleMap map = generateMap(rawMapString, playerPos, enemyPos);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertTrue(player.getCoverType() == CoverType.FULL);
		assertTrue(player.isFlanked());
		assertTrue(player.getDefense(enemy) == CoverType.NONE.getDefense());
	}

	@Test
	public void testVerticalNonFlank() throws BadStateStringException {
		/*
		 * Map looks like:
		 * 0 0 0 0
		 * 0 A 0 0
		 * 0 1 0 0
		 * 0 E 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 1 1";

		GridPosition playerPos = new GridPosition(1, 2);
		GridPosition enemyPos = new GridPosition(1, 0);
		BattleMap map = generateMap(rawMapString, playerPos, enemyPos);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertTrue(player.getCoverType() == CoverType.FULL);
		assertFalse(player.isFlanked());
		assertTrue(player.getDefense(enemy) == CoverType.FULL.getDefense());
	}

	@Test
	public void testPeekFlank() throws BadStateStringException {
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

		GridPosition playerPos = new GridPosition(0, 3);
		GridPosition enemyPos = new GridPosition(1, 0);
		BattleMap map = generateMap(rawMapString, playerPos, enemyPos);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertTrue(player.getCoverType() == CoverType.FULL);
		assertTrue(player.isFlanked());
		assertTrue(player.getDefense(enemy) == CoverType.NONE.getDefense());
	}

	@Test
	public void testPeekFlank2() throws BadStateStringException {
		/*
		 * Map looks like:
		 * A 1 0 0
		 * 0 0 0 0
		 * 0 0 0 0
		 * 0 1 0 0
		 * 0 E 0 0
		 * 1 0 0 0
		 */
		String rawMapString = "6 4\n"
				+ "s 1 5\n"
				+ "s 1 2\n"
				+ "s 0 0";

		GridPosition playerPos = new GridPosition(0, 5);
		GridPosition enemyPos = new GridPosition(1, 1);
		BattleMap map = generateMap(rawMapString, playerPos, enemyPos);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertTrue(player.getCoverType() == CoverType.FULL);
		assertTrue(player.isFlanked());
		assertTrue(player.getDefense(enemy) == CoverType.NONE.getDefense());
	}

	@Test
	public void testClosePeekFlank() throws BadStateStringException {
		/*
		 * Map looks like:
		 * A 1 0 0
		 * 0 E 0 0
		 * 0 0 0 0
		 * 0 0 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 1 3";

		GridPosition playerPos = new GridPosition(0, 3);
		GridPosition enemyPos = new GridPosition(1, 2);
		BattleMap map = generateMap(rawMapString, playerPos, enemyPos);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertTrue(player.getCoverType() == CoverType.FULL);
		assertTrue(player.isFlanked());
		assertTrue(player.getDefense(enemy) == CoverType.NONE.getDefense());
	}

	@Test
	public void testVertDiagonalFlank() throws BadStateStringException {
		/*
		 * Map looks like:
		 * 0 A 1 0
		 * 0 0 0 0
		 * 0 0 0 0
		 * E 0 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 2 3";

		GridPosition playerPos = new GridPosition(1, 3);
		GridPosition enemyPos = new GridPosition(0, 0);
		BattleMap map = generateMap(rawMapString, playerPos, enemyPos);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertTrue(player.getCoverType() == CoverType.FULL);
		assertTrue(player.isFlanked());
		assertTrue(player.isFlankedBy(map.getAgentAtPos(enemyPos)));
		assertTrue(player.getDefense(enemy) == CoverType.NONE.getDefense());
	}

}
