package com.pipai.wf.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pipai.wf.battle.BattleMap;
import com.pipai.wf.battle.BattleMapCell;

public class BattleTest {

	@Test
	public void testMapString() {
		String mapString = "3 3\n"
				+ "0 1 1\n"
				+ "0 1 1\n"
				+ "1 0 0";
		BattleMap map = new BattleMap(mapString);
		//Testing cell solids
		assertFalse(map.getCell(0, 0).isEmpty());
		assertTrue(map.getCell(1, 0).isEmpty());
		assertTrue(map.getCell(2, 0).isEmpty());
		assertTrue(map.getCell(0, 1).isEmpty());
		assertFalse(map.getCell(1, 1).isEmpty());
		assertFalse(map.getCell(2, 1).isEmpty());
		assertTrue(map.getCell(0, 2).isEmpty());
		assertFalse(map.getCell(1, 2).isEmpty());
		assertFalse(map.getCell(2, 2).isEmpty());
		
		//Testing traversable
		assertFalse(map.getCell(0, 2).isTraversable(BattleMapCell.Direction.E));
		assertFalse(map.getCell(0, 2).isTraversable(BattleMapCell.Direction.W));
		assertFalse(map.getCell(0, 2).isTraversable(BattleMapCell.Direction.N));
		assertTrue(map.getCell(0, 2).isTraversable(BattleMapCell.Direction.S));
	}

}
