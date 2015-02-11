package com.pipai.wf.battle;

import java.util.EnumMap;

public class BattleMapCell {
	
	public enum Direction {N, S, E, W};
	
	private boolean solid;
	private EnumMap<Direction, Boolean> walls;
	private EnumMap<Direction, BattleMapCell> neighbors;
	
	public BattleMapCell() {
		this.solid = false;
		this.walls = new EnumMap<Direction, Boolean>(Direction.class);
		this.neighbors = new EnumMap<Direction, BattleMapCell>(Direction.class);
	}
	
	public boolean isEmpty() {
		if (this.solid) {
			return false;
		}
		// TODO: check for unit
		return true;
	}
	
	public boolean hasWall(Direction dir) {
		Boolean wall = this.walls.get(dir);
		if (wall == null) { return false; }
		return wall;
	}
	
	/*
	 * Returns whether or not a unit can move in the indicated direction assuming a unit is in this cell
	 */
	public boolean isTraversable(Direction dir) {
		BattleMapCell neighbor = this.neighbors.get(dir);
		if (neighbor == null) { return false; }
		if (!neighbor.isEmpty()) { return false; }
		if (this.hasWall(dir)) { return false; }
		return true;
	}
	
}