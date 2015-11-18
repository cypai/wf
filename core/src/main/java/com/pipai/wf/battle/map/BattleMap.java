package com.pipai.wf.battle.map;

import java.util.ArrayList;
import java.util.HashMap;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;

public class BattleMap {

	private int m, n; // Size of the map
	private HashMap<String, BattleMapCell> cellMap;
	private ArrayList<Agent> agents;

	private BattleConfiguration config;

	public BattleMap(int m, int n, BattleConfiguration config) {
		agents = new ArrayList<Agent>();
		this.config = config;
		initializeMap(m, n);
	}

	public BattleMap(MapString mapString, BattleConfiguration config) {
		agents = new ArrayList<Agent>();
		m = mapString.getRows();
		n = mapString.getCols();
		initializeMap(m, n);
		for (GridPosition pos : mapString.getSolidPositions()) {
			getCell(pos).setTileEnvironmentObject(new FullCoverIndestructibleObject());
		}
		for (AgentState state : mapString.getAgentStates()) {
			addAgent(state);
		}
	}

	/*
	 * Creates an empty map of size m x n
	 */
	private void initializeMap(int m, int n) {
		this.m = m;
		this.n = n;

		cellMap = new HashMap<String, BattleMapCell>();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				GridPosition cellPos = new GridPosition(i, j);
				BattleMapCell cell = new BattleMapCell(cellPos);
				cellMap.put(coordinatesToKey(cellPos), cell);
				if (i > 0) {
					BattleMapCell west = getCell(new GridPosition(i - 1, j));
					west.setNeighbor(cell, Direction.E);
					cell.setNeighbor(west, Direction.W);
				}
				if (j > 0) {
					BattleMapCell south = getCell(new GridPosition(i, j - 1));
					south.setNeighbor(cell, Direction.N);
					cell.setNeighbor(south, Direction.S);
				}
			}
		}
	}

	public String coordinatesToKey(GridPosition pos) {
		return pos.toString();
	}

	public BattleMapCell getCell(GridPosition pos) {
		return cellMap.get(coordinatesToKey(pos));
	}

	public BattleMapCell getCellInDirection(GridPosition pos, Direction d) {
		GridPosition cellPos = null;
		switch (d) {
		case W:
			cellPos = new GridPosition(pos.x - 1, pos.y);
			break;
		case E:
			cellPos = new GridPosition(pos.x + 1, pos.y);
			break;
		case N:
			cellPos = new GridPosition(pos.x, pos.y + 1);
			break;
		case S:
			cellPos = new GridPosition(pos.x, pos.y - 1);
			break;
		case NW:
			cellPos = new GridPosition(pos.x - 1, pos.y + 1);
			break;
		case NE:
			cellPos = new GridPosition(pos.x + 1, pos.y + 1);
			break;
		case SW:
			cellPos = new GridPosition(pos.x - 1, pos.y - 1);
			break;
		case SE:
			cellPos = new GridPosition(pos.x + 1, pos.y - 1);
			break;
		}
		return cellMap.get(coordinatesToKey(cellPos));
	}

	public Agent getAgentAtPos(GridPosition pos) {
		return getCell(pos).getAgent();
	}

	public void addAgent(AgentState state) {
		Agent agent = new Agent(state, this, config);
		BattleMapCell cell = getCell(agent.getPosition());
		if (cell == null) {
			throw new IllegalArgumentException("Cell " + agent.getPosition().toString() + " does not exist");
		}
		cell.setAgent(agent);
		agents.add(agent);
	}

	public ArrayList<Agent> getAgents() {
		return agents;
	}

	public int getRows() {
		return m;
	}

	public int getCols() {
		return n;
	}

	public MapString getMapString() {
		return new MapString(this);
	}

}