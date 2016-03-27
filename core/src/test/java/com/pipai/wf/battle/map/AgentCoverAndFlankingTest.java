package com.pipai.wf.battle.map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentFactory;
import com.pipai.wf.exception.BadStateStringException;

public class AgentCoverAndFlankingTest {

	private static BattleConfiguration getMockConfig() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		Mockito.when(mockConfig.sightRange()).thenReturn(17);
		return mockConfig;
	}

	private static Agent getDummyAgent(Team team, GridPosition position) {
		AgentFactory factory = new AgentFactory();
		return factory.battleAgentFromStats(team, position, 1, 1, 1, 1, 1, 0);
	}

	private static BattleMap generateMap(String mapString, GridPosition playerPos, GridPosition enemyPos)
			throws BadStateStringException {
		BattleMap map = new BattleMap(new MapString(mapString));
		map.addAgent(getDummyAgent(Team.PLAYER, playerPos));
		map.addAgent(getDummyAgent(Team.ENEMY, enemyPos));
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
		AgentCoverCalculator coverCalc = new AgentCoverCalculator(map, getMockConfig());
		Assert.assertEquals(CoverType.FULL, coverCalc.getCoverType(player));
		Assert.assertTrue(coverCalc.isFlanked(player));
		Assert.assertTrue(coverCalc.isFlankedBy(player, enemy));
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
		AgentCoverCalculator coverCalc = new AgentCoverCalculator(map, getMockConfig());
		Assert.assertEquals(CoverType.FULL, coverCalc.getCoverType(player));
		Assert.assertFalse(coverCalc.isFlanked(player));
		Assert.assertFalse(coverCalc.isFlankedBy(player, enemy));
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
		AgentCoverCalculator coverCalc = new AgentCoverCalculator(map, getMockConfig());
		Assert.assertEquals(CoverType.FULL, coverCalc.getCoverType(player));
		Assert.assertTrue(coverCalc.isFlanked(player));
		Assert.assertTrue(coverCalc.isFlankedBy(player, enemy));
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
		AgentCoverCalculator coverCalc = new AgentCoverCalculator(map, getMockConfig());
		Assert.assertEquals(CoverType.FULL, coverCalc.getCoverType(player));
		Assert.assertTrue(coverCalc.isFlanked(player));
		Assert.assertTrue(coverCalc.isFlankedBy(player, enemy));
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
		AgentCoverCalculator coverCalc = new AgentCoverCalculator(map, getMockConfig());
		Assert.assertEquals(CoverType.FULL, coverCalc.getCoverType(player));
		Assert.assertTrue(coverCalc.isFlanked(player));
		Assert.assertTrue(coverCalc.isFlankedBy(player, enemy));
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
		AgentCoverCalculator coverCalc = new AgentCoverCalculator(map, getMockConfig());
		Assert.assertEquals(CoverType.FULL, coverCalc.getCoverType(player));
		Assert.assertTrue(coverCalc.isFlanked(player));
		Assert.assertTrue(coverCalc.isFlankedBy(player, enemy));
	}

}
