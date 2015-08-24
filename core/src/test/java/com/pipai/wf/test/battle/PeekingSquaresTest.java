package com.pipai.wf.test.battle;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.exception.BadStateStringException;

public class PeekingSquaresTest {

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
        map.addAgent(AgentState.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0));
		Agent a = map.getAgentAtPos(playerPos);
		ArrayList<GridPosition> peekSquares = a.getPeekingSquares();
		assertTrue(peekSquares.contains(a.getPosition()));
		assertTrue(peekSquares.size() == 1);
	}

	@Test
	public void testPeekSquaresOneCover() {
		/*
		 * Map looks like: (with peeking squares marked with x)
		 * 0 1 0
		 * x A x
		 * 0 0 0
		 */
		String rawMapString = "3 3\n"
				+ "s 1 2";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 1);
        map.addAgent(AgentState.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0));
		Agent a = map.getAgentAtPos(playerPos);
		ArrayList<GridPosition> peekSquares = a.getPeekingSquares();
		assertTrue(peekSquares.contains(a.getPosition()));
		assertTrue(peekSquares.contains(new GridPosition(0, 1)));
		assertTrue(peekSquares.contains(new GridPosition(2, 1)));
		assertTrue(peekSquares.size() == 3);
	}

	@Test
	public void testPeekSquaresLineCover() {
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

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 1);
        map.addAgent(AgentState.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0));
		Agent a = map.getAgentAtPos(playerPos);
		ArrayList<GridPosition> peekSquares = a.getPeekingSquares();
		assertTrue(peekSquares.contains(a.getPosition()));
		assertTrue(peekSquares.size() == 1);
	}

	@Test
	public void testPeekSquaresPartialLineCover() {
		/*
		 * Map looks like: (with peeking squares marked with x)
		 * 0 1 1
		 * x A 0
		 * 0 0 0
		 */
		String rawMapString = "3 3\n"
				+ "s 1 2\n"
				+ "s 2 2";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 1);
        map.addAgent(AgentState.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0));
		Agent a = map.getAgentAtPos(playerPos);
		ArrayList<GridPosition> peekSquares = a.getPeekingSquares();
		assertTrue(peekSquares.contains(a.getPosition()));
		assertTrue(peekSquares.contains(new GridPosition(0, 1)));
		assertTrue(peekSquares.size() == 2);
	}

	@Test
	public void testPeekSquaresCornerCover() {
		/*
		 * Map looks like: (with peeking squares marked with x)
		 * 0 1 0
		 * x A 1
		 * 0 x 0
		 */
		String rawMapString = "3 3\n"
				+ "s 1 2\n"
				+ "s 2 1";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 1);
        map.addAgent(AgentState.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0));
		Agent a = map.getAgentAtPos(playerPos);
		ArrayList<GridPosition> peekSquares = a.getPeekingSquares();
		assertTrue(peekSquares.contains(a.getPosition()));
		assertTrue(peekSquares.contains(new GridPosition(0, 1)));
		assertTrue(peekSquares.contains(new GridPosition(1, 0)));
		assertTrue(peekSquares.size() == 3);
	}

	@Test
	public void testPeekSquaresOppositeCover() {
		/*
		 * Map looks like: (with peeking squares marked with x)
		 * 0 1 0
		 * x A x
		 * 0 1 0
		 */
		String rawMapString = "3 3\n"
				+ "s 1 2\n"
				+ "s 1 0";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 1);
        map.addAgent(AgentState.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0));
		Agent a = map.getAgentAtPos(playerPos);
		ArrayList<GridPosition> peekSquares = a.getPeekingSquares();
		assertTrue(peekSquares.contains(a.getPosition()));
		assertTrue(peekSquares.contains(new GridPosition(0, 1)));
		assertTrue(peekSquares.contains(new GridPosition(2, 1)));
		assertTrue(peekSquares.size() == 3);
	}

	@Test
	public void testPeekSquaresMapSideCover() {
		/*
		 * Map looks like: (with peeking squares marked with x)
		 * 0 0 1
		 * 0 x A
		 * 0 0 0
		 */
		String rawMapString = "3 3\n"
				+ "s 2 2";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(2, 1);
        map.addAgent(AgentState.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0));
		Agent a = map.getAgentAtPos(playerPos);
		ArrayList<GridPosition> peekSquares = a.getPeekingSquares();
		assertTrue(peekSquares.contains(a.getPosition()));
		assertTrue(peekSquares.contains(new GridPosition(1, 1)));
		assertTrue(peekSquares.size() == 2);
	}

	@Test
	public void testCloseRangePeek() {
		/*
		 * Map looks like: (with peeking squares marked with x)
		 * E 1 0
		 * x A x
		 * 0 0 0
		 */
		String rawMapString = "3 3\n"
				+ "s 1 2";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 1);
        map.addAgent(AgentState.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0));
		GridPosition enemyPos = new GridPosition(0, 2);
        map.addAgent(AgentState.newBattleAgentState(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		Agent a = map.getAgentAtPos(playerPos);
		ArrayList<GridPosition> peekSquares = a.getPeekingSquares();
		assertTrue(peekSquares.contains(a.getPosition()));
		assertTrue(peekSquares.contains(new GridPosition(0, 1)));
		assertTrue(peekSquares.contains(new GridPosition(2, 1)));
		assertTrue(peekSquares.size() == 3);
	}
	
}
