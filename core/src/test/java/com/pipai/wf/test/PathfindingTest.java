package com.pipai.wf.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;

import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapGraph;

public class PathfindingTest {
	
	@Test
	public void testOneMobilityMovableList() {
		BattleMap map = new BattleMap(3, 4);
		MapGraph graph = new MapGraph(map, new GridPosition(1, 1), 1, 1);
		ArrayList<GridPosition> movableList = graph.getMovableCellPositions();
		ArrayList<GridPosition> req = new ArrayList<GridPosition>();
		req.add(new GridPosition(1, 1));
		req.add(new GridPosition(0, 1));
		req.add(new GridPosition(1, 0));
		req.add(new GridPosition(1, 2));
		req.add(new GridPosition(2, 1));
		for (GridPosition r : req) {
			assertTrue("Does not contain " + r, movableList.contains(r));
		}
		assertTrue(movableList.size() == 5);
	}
	
	@Test
	public void testTwoMobilityMovableList() {
		BattleMap map = new BattleMap(4, 4);
		MapGraph graph = new MapGraph(map, new GridPosition(0, 1), 2, 1);
		ArrayList<GridPosition> movableList = graph.getMovableCellPositions();
		ArrayList<GridPosition> req = new ArrayList<GridPosition>();
		req.add(new GridPosition(0, 1));
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
		assertTrue(movableList.size() == 8);
	}
	
	/*
	 * Checks to see if the list contains a valid path from start to end
	 */
	private boolean checkPathingList(LinkedList<GridPosition> list, GridPosition start, GridPosition end) {
		if (!list.peekFirst().equals(start) || !list.peekLast().equals(end)) {
			return false;
		}
		GridPosition prev = null;
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
		MapGraph graph = new MapGraph(map, start, 10, 1);
		LinkedList<GridPosition> path = graph.getPath(end);
		assertTrue("Invalid path", checkPathingList(path, start, end));
		assertTrue("Path too long: Expected 6 but got " + String.valueOf(path.size()), path.size() == 6);
	}
	
	@Test
	public void testTooFarPathing() {
		BattleMap map = new BattleMap(4, 4);
		GridPosition start = new GridPosition(0, 0);
		GridPosition end = new GridPosition(3, 2);
		MapGraph graph = new MapGraph(map, start, 3, 1);
		LinkedList<GridPosition> path = graph.getPath(end);
		assertTrue(path == null);
	}
	
	@Test
	public void testSameSquarePathing() {
		BattleMap map = new BattleMap(4, 4);
		GridPosition start = new GridPosition(0, 0);
		GridPosition end = new GridPosition(0, 0);
		MapGraph graph = new MapGraph(map, start, 3, 1);
		LinkedList<GridPosition> path = graph.getPath(end);
		assertTrue("Invalid path", checkPathingList(path, start, end));
		assertTrue("Path too long: Expected 1 but got " + String.valueOf(path.size()), path.size() == 1);
	}
}
