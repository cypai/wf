package com.pipai.wf.battle.map;

import org.junit.Assert;
import org.junit.Test;

import com.pipai.wf.exception.BadStateStringException;

public class MapGenerationTest {

	@Test
	public void testMapString() throws BadStateStringException {
		/*
		 * Map looks like:
		 * 0 1 1
		 * 0 0 0
		 */
		String rawMapString = "2 3\n"
				+ "s 1 1\n"
				+ "s 2 1";
		BattleMap map = null;
		map = new BattleMap(new MapString(rawMapString));
		// Testing cell solids
		Assert.assertTrue(map.getCell(new GridPosition(0, 0)).isEmpty());
		Assert.assertTrue(map.getCell(new GridPosition(1, 0)).isEmpty());
		Assert.assertTrue(map.getCell(new GridPosition(2, 0)).isEmpty());
		Assert.assertTrue(map.getCell(new GridPosition(0, 1)).isEmpty());
		Assert.assertFalse(map.getCell(new GridPosition(1, 1)).isEmpty());
		Assert.assertFalse(map.getCell(new GridPosition(2, 1)).isEmpty());

		// Testing traversable
		GridPosition checkPos = new GridPosition(1, 0);
		Assert.assertTrue(map.getCell(checkPos).isTraversable(Direction.E));
		Assert.assertTrue(map.getCell(checkPos).isTraversable(Direction.W));
		Assert.assertFalse(map.getCell(checkPos).isTraversable(Direction.N));
		Assert.assertFalse(map.getCell(checkPos).isTraversable(Direction.S));
	}

}
