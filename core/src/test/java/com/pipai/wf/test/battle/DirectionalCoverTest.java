package com.pipai.wf.test.battle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.Direction;
import com.pipai.wf.battle.map.DirectionalCoverSystem;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.exception.BadStateStringException;

public class DirectionalCoverTest {

	@Test
	public void neededCoverDirectionsTest() {
		GridPosition pos = new GridPosition(1, 1);
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(0, 0)).contains(Direction.S));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(0, 0)).contains(Direction.W));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(0, 1)).contains(Direction.W));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(0, 2)).contains(Direction.W));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(0, 2)).contains(Direction.N));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(1, 2)).contains(Direction.N));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(2, 2)).contains(Direction.N));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(2, 2)).contains(Direction.E));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(2, 1)).contains(Direction.E));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(2, 0)).contains(Direction.E));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(2, 0)).contains(Direction.S));
	}

	@Test
	public void testVerticalFlank() {
		/*
		 * Map looks like:
		 * 0 0 0 0
		 * 0 A 1 0
		 * 0 0 0 0
		 * 0 E 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 2 2";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 2);
		GridPosition enemyPos = new GridPosition(1, 0);
		DirectionalCoverSystem coverSystem = new DirectionalCoverSystem(map);
		assertFalse(coverSystem.isOpen(playerPos));
		assertTrue(coverSystem.getCoverDirections(playerPos).contains(Direction.E));
		assertTrue(coverSystem.getCoverDirections(playerPos).size() == 1);
		assertTrue(coverSystem.isFlankedBy(playerPos, enemyPos));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).contains(Direction.S));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).size() == 1);
	}

	@Test
	public void testVerticalNonFlank() {
		/*
		 * Map looks like:
		 * 0 0 0 0
		 * 0 A 1 0
		 * 0 1 0 0
		 * 0 E 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 2 2\n"
				+ "s 1 1";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 2);
		GridPosition enemyPos = new GridPosition(1, 0);
		DirectionalCoverSystem coverSystem = new DirectionalCoverSystem(map);
		assertFalse(coverSystem.isOpen(playerPos));
		assertTrue(coverSystem.getCoverDirections(playerPos).contains(Direction.E));
		assertTrue(coverSystem.getCoverDirections(playerPos).contains(Direction.S));
		assertTrue(coverSystem.getCoverDirections(playerPos).size() == 2);
		assertFalse(coverSystem.isFlankedBy(playerPos, enemyPos));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).contains(Direction.S));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).size() == 1);
	}

	@Test
	public void testSEFlank() {
		/*
		 * Map looks like:
		 * 0 1 0 0
		 * 1 A 0 0
		 * 0 0 0 0
		 * 0 0 E 0
		 */
		String rawMapString = "4 4\n"
				+ "s 0 2\n"
				+ "s 1 3";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 2);
		GridPosition enemyPos = new GridPosition(2, 0);
		DirectionalCoverSystem coverSystem = new DirectionalCoverSystem(map);
		assertFalse(coverSystem.isOpen(playerPos));
		assertTrue(coverSystem.getCoverDirections(playerPos).contains(Direction.W));
		assertTrue(coverSystem.getCoverDirections(playerPos).contains(Direction.N));
		assertTrue(coverSystem.getCoverDirections(playerPos).size() == 2);
		assertTrue(coverSystem.isFlankedBy(playerPos, enemyPos));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).contains(Direction.S));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).contains(Direction.E));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).size() == 2);
	}

	@Test
	public void testSENonFlank() {
		/*
		 * Map looks like:
		 * 0 A 0 0
		 * 0 1 0 0
		 * 0 0 0 E
		 * 0 0 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 1 2";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 3);
		GridPosition enemyPos = new GridPosition(3, 1);
		DirectionalCoverSystem coverSystem = new DirectionalCoverSystem(map);
		assertFalse(coverSystem.isOpen(playerPos));
		assertTrue(coverSystem.getCoverDirections(playerPos).contains(Direction.S));
		assertTrue(coverSystem.getCoverDirections(playerPos).size() == 1);
		assertFalse(coverSystem.isFlankedBy(playerPos, enemyPos));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).contains(Direction.S));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).contains(Direction.E));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).size() == 2);
	}

	@Test
	public void testVertDiagonalFlank() {
		/*
		 * Map looks like:
		 * 0 A 1 0
		 * 0 0 0 0
		 * 0 0 0 0
		 * E 0 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 2 3";

		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 3);
		GridPosition enemyPos = new GridPosition(0, 0);
		DirectionalCoverSystem coverSystem = new DirectionalCoverSystem(map);
		assertFalse(coverSystem.isOpen(playerPos));
		assertTrue(coverSystem.getCoverDirections(playerPos).contains(Direction.E));
		assertTrue(coverSystem.getCoverDirections(playerPos).size() == 1);
		assertTrue(coverSystem.isFlankedBy(playerPos, enemyPos));
	}

}
