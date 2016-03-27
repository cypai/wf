package com.pipai.wf.battle.map;

import org.junit.Assert;
import org.junit.Test;

import com.pipai.wf.exception.BadStateStringException;

public class DirectionalCoverTest {

	@Test
	public void neededCoverDirectionsTest() {
		GridPosition pos = new GridPosition(1, 1);
		Assert.assertTrue(
				DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(0, 0)).contains(Direction.S));
		Assert.assertTrue(
				DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(0, 0)).contains(Direction.W));
		Assert.assertTrue(
				DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(0, 1)).contains(Direction.W));
		Assert.assertTrue(
				DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(0, 2)).contains(Direction.W));
		Assert.assertTrue(
				DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(0, 2)).contains(Direction.N));
		Assert.assertTrue(
				DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(1, 2)).contains(Direction.N));
		Assert.assertTrue(
				DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(2, 2)).contains(Direction.N));
		Assert.assertTrue(
				DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(2, 2)).contains(Direction.E));
		Assert.assertTrue(
				DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(2, 1)).contains(Direction.E));
		Assert.assertTrue(
				DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(2, 0)).contains(Direction.E));
		Assert.assertTrue(
				DirectionalCoverSystem.getNeededCoverDirections(pos, new GridPosition(2, 0)).contains(Direction.S));
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
			Assert.fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 2);
		GridPosition enemyPos = new GridPosition(1, 0);
		DirectionalCoverSystem coverSystem = new DirectionalCoverSystem(map);
		Assert.assertFalse(coverSystem.isOpen(playerPos));
		Assert.assertTrue(coverSystem.getCoverDirections(playerPos).contains(Direction.E));
		Assert.assertEquals(1, coverSystem.getCoverDirections(playerPos).size());
		Assert.assertTrue(coverSystem.isFlankedBy(playerPos, enemyPos));
		Assert.assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).contains(Direction.S));
		Assert.assertEquals(1, DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).size());
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
			Assert.fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 2);
		GridPosition enemyPos = new GridPosition(1, 0);
		DirectionalCoverSystem coverSystem = new DirectionalCoverSystem(map);
		Assert.assertFalse(coverSystem.isOpen(playerPos));
		Assert.assertTrue(coverSystem.getCoverDirections(playerPos).contains(Direction.E));
		Assert.assertTrue(coverSystem.getCoverDirections(playerPos).contains(Direction.S));
		Assert.assertEquals(2, coverSystem.getCoverDirections(playerPos).size());
		Assert.assertFalse(coverSystem.isFlankedBy(playerPos, enemyPos));
		Assert.assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).contains(Direction.S));
		Assert.assertEquals(1, DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).size());
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
			Assert.fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 2);
		GridPosition enemyPos = new GridPosition(2, 0);
		DirectionalCoverSystem coverSystem = new DirectionalCoverSystem(map);
		Assert.assertFalse(coverSystem.isOpen(playerPos));
		Assert.assertTrue(coverSystem.getCoverDirections(playerPos).contains(Direction.W));
		Assert.assertTrue(coverSystem.getCoverDirections(playerPos).contains(Direction.N));
		Assert.assertEquals(2, coverSystem.getCoverDirections(playerPos).size());
		Assert.assertTrue(coverSystem.isFlankedBy(playerPos, enemyPos));
		Assert.assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).contains(Direction.S));
		Assert.assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).contains(Direction.E));
		Assert.assertEquals(2, DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).size());
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
			Assert.fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 3);
		GridPosition enemyPos = new GridPosition(3, 1);
		DirectionalCoverSystem coverSystem = new DirectionalCoverSystem(map);
		Assert.assertFalse(coverSystem.isOpen(playerPos));
		Assert.assertTrue(coverSystem.getCoverDirections(playerPos).contains(Direction.S));
		Assert.assertEquals(1, coverSystem.getCoverDirections(playerPos).size());
		Assert.assertFalse(coverSystem.isFlankedBy(playerPos, enemyPos));
		Assert.assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).contains(Direction.S));
		Assert.assertTrue(DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).contains(Direction.E));
		Assert.assertEquals(2, DirectionalCoverSystem.getNeededCoverDirections(playerPos, enemyPos).size());
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
			Assert.fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 3);
		GridPosition enemyPos = new GridPosition(0, 0);
		DirectionalCoverSystem coverSystem = new DirectionalCoverSystem(map);
		Assert.assertFalse(coverSystem.isOpen(playerPos));
		Assert.assertTrue(coverSystem.getCoverDirections(playerPos).contains(Direction.E));
		Assert.assertEquals(1, coverSystem.getCoverDirections(playerPos).size());
		Assert.assertTrue(coverSystem.isFlankedBy(playerPos, enemyPos));
	}

}
