package com.pipai.wf.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.Direction;
import com.pipai.wf.battle.map.DirectionalCoverSystem;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.exception.BadStateStringException;

public class DirectionalCoverTest {

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
		assertFalse(DirectionalCoverSystem.isOpen(map, playerPos));
		assertTrue(DirectionalCoverSystem.getCoverDirections(map, playerPos).contains(Direction.E));
		assertTrue(DirectionalCoverSystem.getCoverDirections(map, playerPos).size() == 1);
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).contains(Direction.S));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).size() == 1);
		assertTrue(DirectionalCoverSystem.isFlankedBy(map, playerPos, enemyPos));
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
		assertFalse(DirectionalCoverSystem.isOpen(map, playerPos));
		assertTrue(DirectionalCoverSystem.getCoverDirections(map, playerPos).contains(Direction.E));
		assertTrue(DirectionalCoverSystem.getCoverDirections(map, playerPos).contains(Direction.S));
		assertTrue(DirectionalCoverSystem.getCoverDirections(map, playerPos).size() == 2);
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).contains(Direction.S));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).size() == 1);
		assertFalse(DirectionalCoverSystem.isFlankedBy(map, playerPos, enemyPos));
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
		assertFalse(DirectionalCoverSystem.isOpen(map, playerPos));
		assertTrue(DirectionalCoverSystem.getCoverDirections(map, playerPos).contains(Direction.W));
		assertTrue(DirectionalCoverSystem.getCoverDirections(map, playerPos).contains(Direction.N));
		assertTrue(DirectionalCoverSystem.getCoverDirections(map, playerPos).size() == 2);
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).contains(Direction.S));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).contains(Direction.E));
		assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).size() == 2);
		assertTrue(DirectionalCoverSystem.isFlankedBy(map, playerPos, enemyPos));
	}
	
}
