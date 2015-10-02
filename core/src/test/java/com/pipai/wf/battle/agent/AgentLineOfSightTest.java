package com.pipai.wf.battle.agent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.pipai.libgdx.test.GdxMockedTest;
import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.exception.BadStateStringException;

public class AgentLineOfSightTest extends GdxMockedTest {

	private static BattleMap generateMap(String mapString, GridPosition playerPos, GridPosition enemyPos) throws BadStateStringException {
		BattleMap map = new BattleMap(new MapString(mapString));
		AgentStateFactory factory = new AgentStateFactory(new BattleConfiguration());
		map.addAgent(factory.battleAgentFromStats(Team.PLAYER, playerPos, factory.statsOnlyState(1, 1, 1, 1, 1, 0)));
		map.addAgent(factory.battleAgentFromStats(Team.ENEMY, enemyPos, factory.statsOnlyState(1, 1, 1, 1, 1, 0)));
		return map;
	}

	@Test
	public void testPeekFlankLOS() throws BadStateStringException {
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
		assertTrue(player.canSee(enemy));
		assertTrue(enemy.canSee(player));
	}

	@Test
	public void testDiagonalTopOpenCoverLOS() throws BadStateStringException {
		/*
		 * Map looks like:
		 * 0 0 0 E
		 * 0 1 0 0
		 * 0 0 0 0
		 * A 0 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 1 2";

		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(3, 3);
		BattleMap map = generateMap(rawMapString, playerPos, enemyPos);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertTrue(player.canSee(enemy));
		assertTrue(enemy.canSee(player));
	}

	@Test
	public void testDiagonalBottomOpenCoverLOS() throws BadStateStringException {
		/*
		 * Map looks like:
		 * 0 0 0 E
		 * 0 0 0 0
		 * 0 0 1 0
		 * A 0 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 2 1";

		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(3, 3);
		BattleMap map = generateMap(rawMapString, playerPos, enemyPos);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertTrue(player.canSee(enemy));
		assertTrue(enemy.canSee(player));
	}

	@Test
	public void testDiagonalBlockedCoverLOS() throws BadStateStringException {
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

		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(3, 3);
		BattleMap map = generateMap(rawMapString, playerPos, enemyPos);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertFalse(player.canSee(enemy));
		assertFalse(enemy.canSee(player));
	}

	@Test
	public void testSteepDiagonalBlockedCoverLOS() throws BadStateStringException {
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

		GridPosition playerPos = new GridPosition(1, 0);
		GridPosition enemyPos = new GridPosition(2, 3);
		BattleMap map = generateMap(rawMapString, playerPos, enemyPos);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertFalse(player.canSee(enemy));
		assertFalse(enemy.canSee(player));
	}

	@Test
	public void testFlatDiagonalBlockedCoverLOS() throws BadStateStringException {
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

		GridPosition playerPos = new GridPosition(0, 1);
		GridPosition enemyPos = new GridPosition(3, 2);
		BattleMap map = generateMap(rawMapString, playerPos, enemyPos);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertFalse(player.canSee(enemy));
		assertFalse(enemy.canSee(player));
	}

	@Test
	public void testOpenLOS() {
		BattleConfiguration config = new BattleConfiguration();
		BattleMap map = new BattleMap(1, config.sightRange() + 1);
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(config.sightRange() - 1, 0);
		AgentStateFactory factory = new AgentStateFactory(config);
		map.addAgent(factory.battleAgentFromStats(Team.PLAYER, playerPos, factory.statsOnlyState(1, 1, 1, 1, 1, 1)));
		map.addAgent(factory.battleAgentFromStats(Team.ENEMY, enemyPos, factory.statsOnlyState(1, 1, 1, 1, 1, 1)));
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertTrue(player.canSee(enemy));
		assertTrue(enemy.canSee(player));
	}

	@Test
	public void testTooFarLOS() {
		BattleConfiguration config = new BattleConfiguration();
		BattleMap map = new BattleMap(1, config.sightRange() + 1);
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(config.sightRange(), 0);
		AgentStateFactory factory = new AgentStateFactory(config);
		map.addAgent(factory.battleAgentFromStats(Team.PLAYER, playerPos, factory.statsOnlyState(1, 1, 1, 1, 1, 1)));
		map.addAgent(factory.battleAgentFromStats(Team.ENEMY, enemyPos, factory.statsOnlyState(1, 1, 1, 1, 1, 1)));
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertFalse(player.canSee(enemy));
		assertFalse(enemy.canSee(player));
	}

}
