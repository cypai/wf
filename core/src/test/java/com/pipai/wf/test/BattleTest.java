package com.pipai.wf.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.BattleMapCell;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.Agent;

public class BattleTest {

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
		BattleMap map = new BattleMap(new MapString(rawMapString));
		//Testing cell solids
		assertTrue(map.getCell(new GridPosition(0, 0)).isEmpty());
		assertTrue(map.getCell(new GridPosition(1, 0)).isEmpty());
		assertTrue(map.getCell(new GridPosition(2, 0)).isEmpty());
		assertTrue(map.getCell(new GridPosition(0, 1)).isEmpty());
		assertFalse(map.getCell(new GridPosition(1, 1)).isEmpty());
		assertFalse(map.getCell(new GridPosition(2, 1)).isEmpty());
		
		//Testing traversable
		GridPosition checkPos = new GridPosition(1, 0);
		assertTrue(map.getCell(checkPos).isTraversable(BattleMapCell.Direction.E));
		assertTrue(map.getCell(checkPos).isTraversable(BattleMapCell.Direction.W));
		assertFalse(map.getCell(checkPos).isTraversable(BattleMapCell.Direction.N));
		assertFalse(map.getCell(checkPos).isTraversable(BattleMapCell.Direction.S));
	}
	
	@Test
	public void testAgentMoveAction() {
		/*
		 * Map looks like:
		 * 0 1 1 0
		 * 0 1 0 0
		 * 0 A 0 0
		 */
		String rawMapString = "3 4\n"
				+ "s 1 1\n"
				+ "s 1 2\n"
				+ "s 2 2\n"
				+ "a 1 0";
		BattleMap map = new BattleMap(new MapString(rawMapString));
		Agent agent = map.getAgentAtPos(new GridPosition(1, 0));
		assertFalse(agent == null);
		MoveAction move = new MoveAction(agent, new GridPosition(3, 2));
		move.perform();
		assertTrue(map.getAgentAtPos(new GridPosition(1, 0)) == null);
		assertFalse(map.getAgentAtPos(new GridPosition(3, 2)) == null);
		//Check to see if they are the same object
		assertTrue(agent == map.getAgentAtPos(new GridPosition(3, 2)));
		new MoveAction(agent, new GridPosition(1, 1)).perform();
		assertTrue(map.getAgentAtPos(new GridPosition(1, 0)) == null);
		assertTrue(map.getAgentAtPos(new GridPosition(3, 2)) == null);
		assertFalse(map.getAgentAtPos(new GridPosition(1, 1)) == null);
	}

}
