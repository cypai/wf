package com.pipai.wf.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.exception.BadStateStringException;

public class LineOfSightTest {

	@Test
	public void testHorizontalSupercover() {
		/*
		 * Should look like:
		 * 0 0 0 0
		 * 0 0 0 0
		 * 0 0 0 0
		 * A x x E
		 */
		BattleMap map = new BattleMap(4, 4);
		ArrayList<GridPosition> supercover = map.supercover(new GridPosition(0,0), new GridPosition(3,0)).supercover;
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
		 * Should look like:
		 * E 0 0 0
		 * x 0 0 0
		 * x 0 0 0
		 * A 0 0 0
		 */
		BattleMap map = new BattleMap(4, 4);
		ArrayList<GridPosition> supercover = map.supercover(new GridPosition(0,0), new GridPosition(0,3)).supercover;
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
		 * Should look like:
		 * 0 0 0 E
		 * 0 0 x 0
		 * 0 x 0 0
		 * A 0 0 0
		 */
		BattleMap map = new BattleMap(4, 4);
		ArrayList<GridPosition> supercover = map.supercover(new GridPosition(0,0), new GridPosition(3,3)).supercover;
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
		 * Should look like:
		 * 0 0 0 0
		 * 0 0 0 0
		 * 0 0 x E
		 * A x 0 0
		 */
		BattleMap map = new BattleMap(4, 4);
		ArrayList<GridPosition> supercover = map.supercover(new GridPosition(0,0), new GridPosition(3,1)).supercover;
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
		 * Should look like:
		 * 0 0 E 0
		 * 0 x x 0
		 * x x 0 0
		 * A 0 0 0
		 */
		BattleMap map = new BattleMap(4, 4);
		ArrayList<GridPosition> supercover = map.supercover(new GridPosition(0,0), new GridPosition(2,3)).supercover;
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
				+ "s 2 0";

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
		BattleMap map = new BattleMap(2, 4);
		assertTrue(map.lineOfSight(new GridPosition(0, 0), new GridPosition(3, 0)));
	}
	
}