package com.pipai.wf.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.RangeAttackAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.attack.SimpleRangedAttack;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.exception.BadStateStringException;

public class LineOfSightTest {

	@Test
	public void testHorizontalSupercover() {
		/*
		 * Map looks like:
		 * 0 0 0 0
		 * 0 1 1 0
		 * 0 0 0 0
		 * A 0 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 1 2\n"
				+ "s 2 2\n"
				+ "a 0 0";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		ArrayList<GridPosition> supercover = map.supercover(new GridPosition(0,0), new GridPosition(3,0));
		ArrayList<GridPosition> req = new ArrayList<GridPosition>();
		req.add(new GridPosition(1, 0));
		req.add(new GridPosition(2, 0));
		for (GridPosition r : req) {
			assertTrue("Does not contain " + r, supercover.contains(r));
		}
		assertTrue(supercover.size() == req.size());
	}
	
	@Test
	public void testVerticalSupercover() {
		/*
		 * Map looks like:
		 * 0 0 0 0
		 * 0 1 1 0
		 * 0 0 0 0
		 * A 0 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 1 2\n"
				+ "s 2 2\n"
				+ "a 0 0";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		ArrayList<GridPosition> supercover = map.supercover(new GridPosition(0,0), new GridPosition(0,3));
		ArrayList<GridPosition> req = new ArrayList<GridPosition>();
		req.add(new GridPosition(0, 1));
		req.add(new GridPosition(0, 2));
		for (GridPosition r : req) {
			assertTrue("Does not contain " + r, supercover.contains(r));
		}
		assertTrue(supercover.size() == req.size());
	}
	
	@Test
	public void testDiagonalSupercover() {
		/*
		 * Map looks like:
		 * 0 0 0 0
		 * 0 1 1 0
		 * 0 0 0 0
		 * A 0 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 1 2\n"
				+ "s 2 2\n"
				+ "a 0 0";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		ArrayList<GridPosition> supercover = map.supercover(new GridPosition(0,0), new GridPosition(3,3));
		ArrayList<GridPosition> req = new ArrayList<GridPosition>();
		req.add(new GridPosition(1, 1));
		req.add(new GridPosition(2, 2));
		for (GridPosition r : req) {
			assertTrue("Does not contain " + r, supercover.contains(r));
		}
		assertTrue(supercover.size() == req.size());
	}

	@Test
	public void testFractionalSlopeSupercover() {
		/*
		 * Map looks like:
		 * 0 0 0 0
		 * 0 1 1 0
		 * 0 0 0 0
		 * A 0 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 1 2\n"
				+ "s 2 2\n"
				+ "a 0 0";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		ArrayList<GridPosition> supercover = map.supercover(new GridPosition(0,0), new GridPosition(3,1));
		ArrayList<GridPosition> req = new ArrayList<GridPosition>();
		req.add(new GridPosition(1, 0));
		req.add(new GridPosition(2, 1));
		for (GridPosition r : req) {
			assertTrue("Does not contain " + r, supercover.contains(r));
		}
		assertTrue(supercover.size() == req.size());
	}

	@Test
	public void testSteepSlopeSupercover() {
		/*
		 * Map looks like:
		 * 0 0 0 0
		 * 0 1 1 0
		 * 0 0 0 0
		 * A 0 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 1 2\n"
				+ "s 2 2\n"
				+ "a 0 0";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		ArrayList<GridPosition> supercover = map.supercover(new GridPosition(0,0), new GridPosition(2,3));
		ArrayList<GridPosition> req = new ArrayList<GridPosition>();
		req.add(new GridPosition(0, 1));
		req.add(new GridPosition(1, 1));
		req.add(new GridPosition(1, 2));
		req.add(new GridPosition(2, 2));
		for (GridPosition r : req) {
			assertTrue("Does not contain " + r, supercover.contains(r));
		}
		assertTrue(supercover.size() == req.size());
	}

	@Test
	public void testHorizontalBlockedLOS() {
		/*
		 * Map looks like:
		 * 0 0 0 0
		 * A 0 1 0
		 */
		String rawMapString = "2 4\n"
				+ "s 2 0\n"
				+ "a 0 0";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		assertFalse(map.lineOfSight(new GridPosition(0, 0), new GridPosition(3, 0)));
	}

	@Test
	public void testHorizontalOpenLOS() {
		/*
		 * Map looks like:
		 * 0 0 0 0
		 * A 0 0 0
		 */
		String rawMapString = "2 4\n"
				+ "a 0 0";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		assertTrue(map.lineOfSight(new GridPosition(0, 0), new GridPosition(3, 0)));
	}

	@Test
	public void testPeekSquaresOpen() {
		/*
		 * Map looks like:
		 * 0 0 0
		 * 0 A 0
		 * 0 0 0
		 */
		String rawMapString = "3 3\n"
				+ "a 1 1";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		Agent a = map.getAgentAtPos(new GridPosition(1, 1));
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
				+ "s 1 2\n"
				+ "a 1 1";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		Agent a = map.getAgentAtPos(new GridPosition(1, 1));
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
				+ "s 2 2\n"
				+ "a 1 1";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		Agent a = map.getAgentAtPos(new GridPosition(1, 1));
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
				+ "s 2 2\n"
				+ "a 1 1";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		Agent a = map.getAgentAtPos(new GridPosition(1, 1));
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
				+ "s 2 1\n"
				+ "a 1 1";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		Agent a = map.getAgentAtPos(new GridPosition(1, 1));
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
				+ "s 1 0\n"
				+ "a 1 1";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		Agent a = map.getAgentAtPos(new GridPosition(1, 1));
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
				+ "s 2 2\n"
				+ "a 2 1";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		Agent a = map.getAgentAtPos(new GridPosition(2, 1));
		ArrayList<GridPosition> peekSquares = a.getPeekingSquares();
		assertTrue(peekSquares.contains(a.getPosition()));
		assertTrue(peekSquares.contains(new GridPosition(1, 1)));
		assertTrue(peekSquares.size() == 2);
	}
	
}