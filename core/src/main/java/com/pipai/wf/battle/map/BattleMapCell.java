package com.pipai.wf.battle.map;

import java.util.ArrayList;
import java.util.EnumMap;

import com.pipai.wf.battle.agent.Agent;

public class BattleMapCell {
	
	private EnvironmentObject tileObject;
	private EnumMap<Direction, EnvironmentObject> walls;
	private EnumMap<Direction, BattleMapCell> neighbors;
	private ArrayList<Agent> inactiveAgents;
	private Agent agent;
	private GridPosition position;
	
	public BattleMapCell(GridPosition pos) {
		this.tileObject = null;
		this.walls = new EnumMap<Direction, EnvironmentObject>(Direction.class);
		this.neighbors = new EnumMap<Direction, BattleMapCell>(Direction.class);
		this.position = pos;
		this.inactiveAgents = new ArrayList<Agent>();
	}
	
	public GridPosition getPosition() { return this.position; }
	
	public void setNeighbor(BattleMapCell cell, Direction dir) {
		this.neighbors.put(dir, cell);
	}
	
	public void setWallEnvironmentObject(EnvironmentObject wall, Direction dir) {
		this.walls.put(dir, wall);
	}
	
	public void setTileEnvironmentObject(EnvironmentObject obj) {
		this.tileObject = obj;
	}
	
	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	public void removeAgent() {
		this.agent = null;
	}
	
	public Agent getAgent() {
		return this.agent;
	}
	
	public void makeAgentInactive() {
		this.inactiveAgents.add(this.agent);
		this.agent = null;
	}
	
	public boolean hasAgent() { return this.agent != null; }
	
	public boolean isEmpty() {
		if (this.tileObject != null || this.agent != null) {
			return false;
		}
		return true;
	}
	
	public boolean hasTileSightBlocker() {
		if (this.tileObject != null) {
			return this.tileObject.getCoverType() == CoverType.FULL;
		} else {
			return false;
		}
	}
	
	public EnvironmentObject getTileEnvironmentObject() {
		return tileObject;
	}
	
	public boolean hasWall(Direction dir) {
		return this.walls.get(dir) != null;
	}
	
	public EnvironmentObject getWallEnvironmentObject(Direction dir) {
		return this.walls.get(dir);
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