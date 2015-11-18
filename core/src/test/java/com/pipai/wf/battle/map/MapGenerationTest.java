package com.pipai.wf.battle.map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.exception.BadStateStringException;

public class MapGenerationTest {

	@Test
	public void testMapString() {
		/*
		 * Map looks like:
		 * 0 1 1
		 * 0 0 0
		 */
		String rawMapString = "2 3\n"
				+ "s 1 1\n"
				+ "s 2 1";
		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString), Mockito.mock(BattleConfiguration.class));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		// Testing cell solids
		assertTrue(map.getCell(new GridPosition(0, 0)).isEmpty());
		assertTrue(map.getCell(new GridPosition(1, 0)).isEmpty());
		assertTrue(map.getCell(new GridPosition(2, 0)).isEmpty());
		assertTrue(map.getCell(new GridPosition(0, 1)).isEmpty());
		assertFalse(map.getCell(new GridPosition(1, 1)).isEmpty());
		assertFalse(map.getCell(new GridPosition(2, 1)).isEmpty());

		// Testing traversable
		GridPosition checkPos = new GridPosition(1, 0);
		assertTrue(map.getCell(checkPos).isTraversable(Direction.E));
		assertTrue(map.getCell(checkPos).isTraversable(Direction.W));
		assertFalse(map.getCell(checkPos).isTraversable(Direction.N));
		assertFalse(map.getCell(checkPos).isTraversable(Direction.S));
	}

}
