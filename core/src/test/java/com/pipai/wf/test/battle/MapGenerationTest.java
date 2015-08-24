package com.pipai.wf.test.battle;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.Direction;
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
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		//Testing cell solids
		assertTrue(map.getCell(new GridPosition(0, 0)).isEmpty());
		assertTrue(map.getCell(new GridPosition(1, 0)).isEmpty());
		assertTrue(map.getCell(new GridPosition(2, 0)).isEmpty());
		assertTrue(map.getCell(new GridPosition(0, 1)).isEmpty());
		assertFalse(map.getCell(new GridPosition(1, 1)).isEmpty());
		assertFalse(map.getCell(new GridPosition(2, 1)).isEmpty());
		
		//Testing traversable
		GridPosition checkPos = new GridPosition(1, 0);
		assertTrue(map.getCell(checkPos).isTraversable(Direction.E));
		assertTrue(map.getCell(checkPos).isTraversable(Direction.W));
		assertFalse(map.getCell(checkPos).isTraversable(Direction.N));
		assertFalse(map.getCell(checkPos).isTraversable(Direction.S));
	}

}
