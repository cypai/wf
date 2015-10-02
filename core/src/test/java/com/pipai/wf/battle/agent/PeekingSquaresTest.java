package com.pipai.wf.battle.agent;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Test;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.exception.BadStateStringException;

public class PeekingSquaresTest {

	private static BattleMap generateMap(String mapString, GridPosition playerPos) throws BadStateStringException {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		when(mockConfig.sightRange()).thenReturn(17);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		BattleMap map = new BattleMap(new MapString(mapString));
		map.addAgent(factory.battleAgentFromStats(Team.PLAYER, playerPos, factory.statsOnlyState(1, 1, 1, 1, 1, 0)));
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
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		BattleMap map = new BattleMap(3, 3);
		GridPosition playerPos = new GridPosition(1, 1);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		map.addAgent(factory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0));
		Agent a = map.getAgentAtPos(playerPos);
		ArrayList<GridPosition> peekSquares = a.getPeekingSquares();
		assertTrue(peekSquares.contains(a.getPosition()));
		assertTrue(peekSquares.size() == 1);
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
		ArrayList<GridPosition> peekSquares = a.getPeekingSquares();
		assertTrue(peekSquares.contains(a.getPosition()));
		assertTrue(peekSquares.contains(new GridPosition(0, 1)));
		assertTrue(peekSquares.contains(new GridPosition(2, 1)));
		assertTrue(peekSquares.size() == 3);
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
		ArrayList<GridPosition> peekSquares = a.getPeekingSquares();
		assertTrue(peekSquares.contains(a.getPosition()));
		assertTrue(peekSquares.size() == 1);
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
		ArrayList<GridPosition> peekSquares = a.getPeekingSquares();
		assertTrue(peekSquares.contains(a.getPosition()));
		assertTrue(peekSquares.contains(new GridPosition(0, 1)));
		assertTrue(peekSquares.size() == 2);
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
		ArrayList<GridPosition> peekSquares = a.getPeekingSquares();
		assertTrue(peekSquares.contains(a.getPosition()));
		assertTrue(peekSquares.contains(new GridPosition(0, 1)));
		assertTrue(peekSquares.contains(new GridPosition(1, 0)));
		assertTrue(peekSquares.size() == 3);
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
		ArrayList<GridPosition> peekSquares = a.getPeekingSquares();
		assertTrue(peekSquares.contains(a.getPosition()));
		assertTrue(peekSquares.contains(new GridPosition(0, 1)));
		assertTrue(peekSquares.contains(new GridPosition(2, 1)));
		assertTrue(peekSquares.size() == 3);
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
		ArrayList<GridPosition> peekSquares = a.getPeekingSquares();
		assertTrue(peekSquares.contains(a.getPosition()));
		assertTrue(peekSquares.contains(new GridPosition(1, 1)));
		assertTrue(peekSquares.size() == 2);
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
		AgentStateFactory factory = new AgentStateFactory(mock(BattleConfiguration.class));
		map.addAgent(factory.newBattleAgentState(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		Agent a = map.getAgentAtPos(playerPos);
		ArrayList<GridPosition> peekSquares = a.getPeekingSquares();
		assertTrue(peekSquares.contains(a.getPosition()));
		assertTrue(peekSquares.contains(new GridPosition(0, 1)));
		assertTrue(peekSquares.contains(new GridPosition(2, 1)));
		assertTrue(peekSquares.size() == 3);
	}

}
