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
		tileObject = null;
		walls = new EnumMap<Direction, EnvironmentObject>(Direction.class);
		neighbors = new EnumMap<Direction, BattleMapCell>(Direction.class);
		position = pos;
		inactiveAgents = new ArrayList<Agent>();
	}

	public GridPosition getPosition() {
		return position;
	}

	public void setNeighbor(BattleMapCell cell, Direction dir) {
		neighbors.put(dir, cell);
	}

	public void setWallEnvironmentObject(EnvironmentObject wall, Direction dir) {
		walls.put(dir, wall);
	}

	public void setTileEnvironmentObject(EnvironmentObject obj) {
		tileObject = obj;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public void removeAgent() {
		agent = null;
	}

	public Agent getAgent() {
		return agent;
	}

	public void makeAgentInactive() {
		inactiveAgents.add(agent);
		agent = null;
	}

	public boolean hasAgent() {
		return agent != null;
	}

	public boolean isEmpty() {
		if (tileObject != null || agent != null) {
			return false;
		}
		return true;
	}

	public boolean hasTileSightBlocker() {
		if (tileObject != null) {
			return tileObject.getCoverType() == CoverType.FULL;
		} else {
			return false;
		}
	}

	public EnvironmentObject getTileEnvironmentObject() {
		return tileObject;
	}

	public boolean hasWall(Direction dir) {
		return walls.get(dir) != null;
	}

	public EnvironmentObject getWallEnvironmentObject(Direction dir) {
		return walls.get(dir);
	}

	/*
	 * Returns whether or not a unit can move in the indicated direction assuming a unit is in this cell
	 */
	public boolean isTraversable(Direction dir) {
		BattleMapCell neighbor = neighbors.get(dir);
		if (neighbor == null) {
			return false;
		}
		if (!neighbor.isEmpty()) {
			return false;
		}
		if (hasWall(dir)) {
			return false;
		}
		return true;
	}

}