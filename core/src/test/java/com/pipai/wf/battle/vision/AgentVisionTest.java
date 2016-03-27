package com.pipai.wf.battle.vision;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.libgdx.test.GdxMockedTest;
import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.exception.BadStateStringException;

public class AgentVisionTest extends GdxMockedTest {

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

	private static void performMutuallyVisibleTest(String rawMapString, GridPosition playerPos, GridPosition enemyPos)
			throws BadStateStringException {
		BattleMap map = generateMap(rawMapString, playerPos, enemyPos);
		AgentVisionCalculator agentVisionCalc = new AgentVisionCalculator(map, getMockConfig());
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		Assert.assertTrue(agentVisionCalc.canSee(player, enemy));
		Assert.assertTrue(agentVisionCalc.canSee(enemy, player));
	}

	private static void performMutuallyNotVisibleTest(String rawMapString, GridPosition playerPos,
			GridPosition enemyPos)
			throws BadStateStringException {
		BattleMap map = generateMap(rawMapString, playerPos, enemyPos);
		AgentVisionCalculator agentVisionCalc = new AgentVisionCalculator(map, getMockConfig());
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		Assert.assertFalse(agentVisionCalc.canSee(player, enemy));
		Assert.assertFalse(agentVisionCalc.canSee(enemy, player));
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
		performMutuallyVisibleTest(rawMapString, playerPos, enemyPos);
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
		performMutuallyVisibleTest(rawMapString, playerPos, enemyPos);
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
		performMutuallyVisibleTest(rawMapString, playerPos, enemyPos);
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
		performMutuallyNotVisibleTest(rawMapString, playerPos, enemyPos);
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
		performMutuallyNotVisibleTest(rawMapString, playerPos, enemyPos);
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
		performMutuallyNotVisibleTest(rawMapString, playerPos, enemyPos);
	}

	@Test
	public void testOpenLOS() {
		BattleConfiguration config = new BattleConfiguration();
		BattleMap map = new BattleMap(1, config.sightRange() + 1);
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(config.sightRange() - 1, 0);
		map.addAgent(getDummyAgent(Team.PLAYER, playerPos));
		map.addAgent(getDummyAgent(Team.ENEMY, enemyPos));
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		AgentVisionCalculator agentVisionCalc = new AgentVisionCalculator(map, config);
		Assert.assertTrue(agentVisionCalc.canSee(player, enemy));
		Assert.assertTrue(agentVisionCalc.canSee(enemy, player));
	}

	@Test
	public void testTooFarLOS() {
		BattleConfiguration config = new BattleConfiguration();
		BattleMap map = new BattleMap(1, config.sightRange() + 1);
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(config.sightRange(), 0);
		map.addAgent(getDummyAgent(Team.PLAYER, playerPos));
		map.addAgent(getDummyAgent(Team.ENEMY, enemyPos));
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		AgentVisionCalculator agentVisionCalc = new AgentVisionCalculator(map, config);
		Assert.assertFalse(agentVisionCalc.canSee(player, enemy));
		Assert.assertFalse(agentVisionCalc.canSee(enemy, player));
	}

}
