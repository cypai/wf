package com.pipai.wf.battle.vision;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.battle.map.PeekingSquaresCalculator;
import com.pipai.wf.exception.BadStateStringException;

public class PeekingSquaresTest {

	private static BattleMap generateMap(String mapString, GridPosition playerPos) throws BadStateStringException {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		Mockito.when(mockConfig.sightRange()).thenReturn(17);
		AgentStateFactory factory = new AgentStateFactory();
		BattleMap map = new BattleMap(new MapString(mapString));
		map.addAgent(factory.battleAgentFromStats(Team.PLAYER, playerPos, 1, 1, 1, 1, 1, 0));
		return map;
	}

	@Test
	public void testPeekSquaresOpen() {
		/*
		 * Map looks like:
		 * 0 0 0
		 * 0 A 0
		 * 0 0 0
		 */
		BattleMap map = new BattleMap(3, 3);
		GridPosition playerPos = new GridPosition(1, 1);
		AgentStateFactory factory = new AgentStateFactory();
		map.addAgent(factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0));
		Agent a = map.getAgentAtPos(playerPos);
		PeekingSquaresCalculator peekingCalc = new PeekingSquaresCalculator(map);
		List<GridPosition> peekSquares = peekingCalc.getPeekingSquares(a);
		Assert.assertTrue(peekSquares.contains(a.getPosition()));
		Assert.assertEquals(1, peekSquares.size());
	}

	@Test
	public void testPeekSquaresOneCover() throws BadStateStringException {
		/*
		 * Map looks like: (with peeking squares marked with x)
		 * 0 1 0
		 * x A x
		 * 0 0 0
		 */
		String rawMapString = "3 3\n"
				+ "s 1 2";

		GridPosition playerPos = new GridPosition(1, 1);
		BattleMap map = generateMap(rawMapString, playerPos);
		Agent a = map.getAgentAtPos(playerPos);
		PeekingSquaresCalculator peekingCalc = new PeekingSquaresCalculator(map);
		List<GridPosition> peekSquares = peekingCalc.getPeekingSquares(a);
		Assert.assertTrue(peekSquares.contains(a.getPosition()));
		Assert.assertTrue(peekSquares.contains(new GridPosition(0, 1)));
		Assert.assertTrue(peekSquares.contains(new GridPosition(2, 1)));
		Assert.assertEquals(3, peekSquares.size());
	}

	@Test
	public void testPeekSquaresLineCover() throws BadStateStringException {
		/*
		 * Map looks like: (with peeking squares marked with x)
		 * 1 1 1
		 * 0 A 0
		 * 0 0 0
		 */
		String rawMapString = "3 3\n"
				+ "s 0 2\n"
				+ "s 1 2\n"
				+ "s 2 2";

		GridPosition playerPos = new GridPosition(1, 1);
		BattleMap map = generateMap(rawMapString, playerPos);
		Agent a = map.getAgentAtPos(playerPos);
		PeekingSquaresCalculator peekingCalc = new PeekingSquaresCalculator(map);
		List<GridPosition> peekSquares = peekingCalc.getPeekingSquares(a);
		Assert.assertTrue(peekSquares.contains(a.getPosition()));
		Assert.assertEquals(1, peekSquares.size());
	}

	@Test
	public void testPeekSquaresPartialLineCover() throws BadStateStringException {
		/*
		 * Map looks like: (with peeking squares marked with x)
		 * 0 1 1
		 * x A 0
		 * 0 0 0
		 */
		String rawMapString = "3 3\n"
				+ "s 1 2\n"
				+ "s 2 2";

		GridPosition playerPos = new GridPosition(1, 1);
		BattleMap map = generateMap(rawMapString, playerPos);
		Agent a = map.getAgentAtPos(playerPos);
		PeekingSquaresCalculator peekingCalc = new PeekingSquaresCalculator(map);
		List<GridPosition> peekSquares = peekingCalc.getPeekingSquares(a);
		Assert.assertTrue(peekSquares.contains(a.getPosition()));
		Assert.assertTrue(peekSquares.contains(new GridPosition(0, 1)));
		Assert.assertEquals(2, peekSquares.size());
	}

	@Test
	public void testPeekSquaresCornerCover() throws BadStateStringException {
		/*
		 * Map looks like: (with peeking squares marked with x)
		 * 0 1 0
		 * x A 1
		 * 0 x 0
		 */
		String rawMapString = "3 3\n"
				+ "s 1 2\n"
				+ "s 2 1";

		GridPosition playerPos = new GridPosition(1, 1);
		BattleMap map = generateMap(rawMapString, playerPos);
		Agent a = map.getAgentAtPos(playerPos);
		PeekingSquaresCalculator peekingCalc = new PeekingSquaresCalculator(map);
		List<GridPosition> peekSquares = peekingCalc.getPeekingSquares(a);
		Assert.assertTrue(peekSquares.contains(a.getPosition()));
		Assert.assertTrue(peekSquares.contains(new GridPosition(0, 1)));
		Assert.assertTrue(peekSquares.contains(new GridPosition(1, 0)));
		Assert.assertEquals(3, peekSquares.size());
	}

	@Test
	public void testPeekSquaresOppositeCover() throws BadStateStringException {
		/*
		 * Map looks like: (with peeking squares marked with x)
		 * 0 1 0
		 * x A x
		 * 0 1 0
		 */
		String rawMapString = "3 3\n"
				+ "s 1 2\n"
				+ "s 1 0";

		GridPosition playerPos = new GridPosition(1, 1);
		BattleMap map = generateMap(rawMapString, playerPos);
		Agent a = map.getAgentAtPos(playerPos);
		PeekingSquaresCalculator peekingCalc = new PeekingSquaresCalculator(map);
		List<GridPosition> peekSquares = peekingCalc.getPeekingSquares(a);
		Assert.assertTrue(peekSquares.contains(a.getPosition()));
		Assert.assertTrue(peekSquares.contains(new GridPosition(0, 1)));
		Assert.assertTrue(peekSquares.contains(new GridPosition(2, 1)));
		Assert.assertEquals(3, peekSquares.size());
	}

	@Test
	public void testPeekSquaresMapSideCover() throws BadStateStringException {
		/*
		 * Map looks like: (with peeking squares marked with x)
		 * 0 0 1
		 * 0 x A
		 * 0 0 0
		 */
		String rawMapString = "3 3\n"
				+ "s 2 2";

		GridPosition playerPos = new GridPosition(2, 1);
		BattleMap map = generateMap(rawMapString, playerPos);
		Agent a = map.getAgentAtPos(playerPos);
		PeekingSquaresCalculator peekingCalc = new PeekingSquaresCalculator(map);
		List<GridPosition> peekSquares = peekingCalc.getPeekingSquares(a);
		Assert.assertTrue(peekSquares.contains(a.getPosition()));
		Assert.assertTrue(peekSquares.contains(new GridPosition(1, 1)));
		Assert.assertEquals(2, peekSquares.size());
	}

	@Test
	public void testCloseRangePeek() throws BadStateStringException {
		/*
		 * Map looks like: (with peeking squares marked with x)
		 * E 1 0
		 * x A x
		 * 0 0 0
		 */
		String rawMapString = "3 3\n"
				+ "s 1 2";

		GridPosition playerPos = new GridPosition(1, 1);
		BattleMap map = generateMap(rawMapString, playerPos);
		GridPosition enemyPos = new GridPosition(0, 2);
		AgentStateFactory factory = new AgentStateFactory();
		map.addAgent(factory.battleAgentFromStats(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		Agent a = map.getAgentAtPos(playerPos);
		PeekingSquaresCalculator peekingCalc = new PeekingSquaresCalculator(map);
		List<GridPosition> peekSquares = peekingCalc.getPeekingSquares(a);
		Assert.assertTrue(peekSquares.contains(a.getPosition()));
		Assert.assertTrue(peekSquares.contains(new GridPosition(0, 1)));
		Assert.assertTrue(peekSquares.contains(new GridPosition(2, 1)));
		Assert.assertEquals(3, peekSquares.size());
	}

}
