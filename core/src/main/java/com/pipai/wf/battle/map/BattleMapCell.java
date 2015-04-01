package com.pipai.wf.battle.map;

import java.util.ArrayList;
import java.util.EnumMap;

import com.pipai.wf.battle.agent.Agent;

public class BattleMapCell {
	
	private boolean solid;
	private EnumMap<Direction, Boolean> walls;
	private EnumMap<Direction, BattleMapCell> neighbors;
	private ArrayList<Agent> inactiveAgents;
	private Agent agent;
	private GridPosition position;
	
	public BattleMapCell(GridPosition pos) {
		this.solid = false;
		this.walls = new EnumMap<Direction, Boolean>(Direction.class);
		this.neighbors = new EnumMap<Direction, BattleMapCell>(Direction.class);
		this.position = pos;
		this.inactiveAgents = new ArrayList<Agent>();
	}
	
	public GridPosition getPosition() { return this.position; }
	
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