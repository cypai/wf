package com.pipai.wf.battle.map;

import java.util.EnumMap;
import com.pipai.wf.battle.Agent;

public class BattleMapCell {
	
	public enum Direction {N, S, E, W};
	
	private boolean solid;
	private EnumMap<Direction, Boolean> walls;
	private EnumMap<Direction, BattleMapCell> neighbors;
	private Agent agent;
	private Position position;
	
	public BattleMapCell(Position position) {
		this.position = position;
		this.solid = false;
		this.walls = new EnumMap<Direction, Boolean>(Direction.class);
		this.neighbors = new EnumMap<Direction, BattleMapCell>(Direction.class);
	}
	
	public void setNeighbor(BattleMapCell cell, Direction dir) {
		this.neighbors.put(dir, cell);
	}
	
	public void setWall(boolean wall, Direction dir) {
		this.walls.put(dir, wall);
	}
	
	public void setSolid(boolean solid) {
		this.solid = solid;
	}
	
	public void setAgent(Agent agent) {
		agent.setCell(this, this.position);
		this.agent = agent;
	}
	
	public void removeAgent() {
		if (this.agent != null) {
			Agent temp = this.agent;
			this.agent = null;
			temp.removeFromCell();
		}
	}
	
	public Agent getAgent() {
		return this.agent;
	}
	
	public boolean isSolid() { return this.solid; }
	public boolean hasAgent() { return this.agent != null; }
	
	public boolean isEmpty() {
		if (this.solid || this.agent != null) {
			return false;
		}
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