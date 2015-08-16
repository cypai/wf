package com.pipai.wf.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapGraph;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.exception.BadStateStringException;

public class PathfindingTest {
	
	@Test
	public void testOneMobilityMovableList() {
		BattleMap map = new BattleMap(3, 4);
		MapGraph graph = new MapGraph(map, new GridPosition(1, 1), 1, 1, 1);
		ArrayList<GridPosition> movableList = graph.getMovableCellPositions(1);
		ArrayList<GridPosition> req = new ArrayList<GridPosition>();
		req.add(new GridPosition(0, 1));
		req.add(new GridPosition(1, 0));
		req.add(new GridPosition(1, 2));
		req.add(new GridPosition(2, 1));
		for (GridPosition r : req) {
			assertTrue("Does not contain " + r, movableList.contains(r));
		}
		assertTrue(movableList.size() == req.size());
	}
	
	@Test
	public void testTwoMobilityMovableList() {
		BattleMap map = new BattleMap(4, 4);
		MapGraph graph = new MapGraph(map, new GridPosition(0, 1), 2, 1, 1);
		ArrayList<GridPosition> movableList = graph.getMovableCellPositions(1);
		ArrayList<GridPosition> req = new ArrayList<GridPosition>();
		req.add(new GridPosition(0, 0));
		req.add(new GridPosition(0, 2));
		req.add(new GridPosition(0, 3));
		req.add(new GridPosition(1, 1));
		req.add(new GridPosition(2, 1));
		req.add(new GridPosition(1, 2));
		req.add(new GridPosition(1, 0));
		for (GridPosition r : req) {
			assertTrue("Does not contain " + r, movableList.contains(r));
		}
		assertTrue(movableList.size() == req.size());
	}
	
	@Test
	public void testObstacleMovableList() {
		/*
		 * Map looks like:
		 * 0 0 0 0
		 * 0 1 1 0
		 * 0 A 0 0
		 * 0 0 0 1
		 */
		String rawMapString = "4 4\n"
				+ "s 3 0\n"
				+ "s 1 2\n"
				+ "s 2 2";
		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		MapGraph graph = new MapGraph(map, new GridPosition(1, 1), 3, 1, 1);
		ArrayList<GridPosition> movableList = graph.getMovableCellPositions(1);
		ArrayList<GridPosition> req = new ArrayList<GridPosition>();
		req.add(new GridPosition(0, 0));
		req.add(new GridPosition(0, 1));
		req.add(new GridPosition(0, 2));
		req.add(new GridPosition(0, 3));
		req.add(new GridPosition(1, 0));
		req.add(new GridPosition(1, 3));
		req.add(new GridPosition(2, 0));
		req.add(new GridPosition(2, 1));
		req.add(new GridPosition(3, 1));
		req.add(new GridPosition(3, 2));
		for (GridPosition r : req) {
			assertTrue("Does not contain " + r, movableList.contains(r));
		}
		assertTrue(movableList.size() == req.size());
	}
	
	@Test
	public void testThreeMobilityMovableList() {
		BattleMap map = new BattleMap(8, 8);
		MapGraph graph = new MapGraph(map, new GridPosition(3, 3), 3, 1, 1);
		ArrayList<GridPosition> movableList = graph.getMovableCellPositions(1);
		ArrayList<GridPosition> req = new ArrayList<GridPosition>();
		req.add(new GridPosition(0, 3));
		req.add(new GridPosition(1, 3));
		req.add(new GridPosition(2, 3));
		req.add(new GridPosition(4, 3));
		req.add(new GridPosition(5, 3));
		req.add(new GridPosition(6, 3));
		req.add(new GridPosition(3, 0));
		req.add(new GridPosition(3, 1));
		req.add(new GridPosition(3, 2));
		req.add(new GridPosition(3, 4));
		req.add(new GridPosition(3, 5));
		req.add(new GridPosition(3, 6));
		req.add(new GridPosition(1, 1));
		req.add(new GridPosition(1, 5));
		req.add(new GridPosition(5, 1));
		req.add(new GridPosition(5, 5));
		for (GridPosition r : req) {
			assertTrue("Does not contain " + r, movableList.contains(r));
		}
		//assertTrue(movableList.size() == req.size());
	}
	
	/*
	 * Checks to see if the list contains a valid path from start to end
	 */
	private boolean checkPathingList(LinkedList<GridPosition> list, GridPosition start, GridPosition end) {
		if (!list.peekLast().equals(end)) {
			return false;
		}
		GridPosition prev = start;
		for (GridPosition pos : list) {
			if (prev != null) {
				if (Math.abs(prev.x - pos.x) > 1 || Math.abs(prev.y - pos.y) > 1) {
					return false;
				}
			}
			prev = pos;
		}
		return true;
	}
	
	@Test
	public void testCorrectPathing() {
		BattleMap map = new BattleMap(4, 4);
		GridPosition start = new GridPosition(0, 0);
		GridPosition end = new GridPosition(3, 2);
		MapGraph graph = new MapGraph(map, start, 10, 1, 1);
		LinkedList<GridPosition> path = graph.getPath(end);
		assertTrue("Invalid path", checkPathingList(path, start, end));
		assertTrue("Path too long: Expected 4 but got " + String.valueOf(path.size()), path.size() == 4);
	}
	
	@Test
	public void testTooFarPathing() {
		BattleMap map = new BattleMap(4, 4);
		GridPosition start = new GridPosition(0, 0);
		GridPosition end = new GridPosition(3, 2);
		MapGraph graph = new MapGraph(map, start, 3, 1, 1);
		LinkedList<GridPosition> path = graph.getPath(end);
		assertTrue(path == null);
	}
	
	@Test
	public void testCannotMoveToNonEmpty() {
		String rawMapString = "4 4\n"
				+ "s 3 0";
		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
        map.addAgent(AgentState.newBattleAgentState(Team.PLAYER, new GridPosition(1, 1), 3, 5, 2, 5, 65, 0));
        map.addAgent(AgentState.newBattleAgentState(Team.PLAYER, new GridPosition(2, 1), 3, 5, 2, 5, 65, 0));
		MapGraph graph = new MapGraph(map, new GridPosition(1, 1), 10, 1, 1);
		assertFalse("Failed to return false on moving to solid tile", graph.canMoveTo(new GridPosition(3, 0)));
		assertFalse("Failed to return false on moving to tile with other agent", graph.canMoveTo(new GridPosition(2, 1)));
	}
	
	@Test
	public void testIllegalSquares() {
		BattleMap map = new BattleMap(4, 4);
		MapGraph graph = new MapGraph(map, new GridPosition(1, 1), 10, 1, 1);
		GridPosition illegal = new GridPosition(4, 1);
		assertFalse("Failed to return false on moving to tile outside map", graph.canMoveTo(illegal));
		assertTrue("Failed to return null on path to tile outside map", graph.getPath(illegal) == null);
	}
}
