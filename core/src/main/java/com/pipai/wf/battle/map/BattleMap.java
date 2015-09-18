package com.pipai.wf.battle.map;

import java.util.ArrayList;
import java.util.HashMap;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.log.BattleEventLoggable;
import com.pipai.wf.battle.log.BattleLog;

public class BattleMap implements BattleEventLoggable {

	private int m, n; // Size of the map
	private HashMap<String, BattleMapCell> cellMap;
	private ArrayList<Agent> agents;
	private BattleLog log;

	public BattleMap(int m, int n) {
		this.agents = new ArrayList<Agent>();
		this.log = null;
		initializeMap(m, n);
	}

	public BattleMap(MapString mapString) {
		this.agents = new ArrayList<Agent>();
		this.m = mapString.getRows();
		this.n = mapString.getCols();
		initializeMap(this.m, this.n);
		for (GridPosition pos : mapString.getSolidPositions()) {
			this.getCell(pos).setTileEnvironmentObject(new FullCoverIndestructibleObject());
		}
		for (AgentState state : mapString.getAgentStates()) {
			this.addAgent(state);
		}
	}

	/*
	 * Creates an empty map of size m x n
	 */
	private void initializeMap(int m, int n) {
		this.m = m;
		this.n = n;

		this.cellMap = new HashMap<String, BattleMapCell>();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				GridPosition cellPos = new GridPosition(i, j);
				BattleMapCell cell = new BattleMapCell(cellPos);
				this.cellMap.put(this.coordinatesToKey(cellPos), cell);
				if (i > 0) {
					BattleMapCell west = this.getCell(new GridPosition(i - 1, j));
					west.setNeighbor(cell, Direction.E);
					cell.setNeighbor(west, Direction.W);
				}
				if (j > 0) {
					BattleMapCell south = this.getCell(new GridPosition(i, j - 1));
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
		return this.cellMap.get(this.coordinatesToKey(pos));
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
		return this.cellMap.get(this.coordinatesToKey(cellPos));
	}

	public Agent getAgentAtPos(GridPosition pos) {
		return this.getCell(pos).getAgent();
	}

	public void addAgent(AgentState state) {
		Agent agent = new Agent(this, state);
		BattleMapCell cell = this.getCell(agent.getPosition());
		if (cell == null) {
			throw new IllegalArgumentException("Cell " + agent.getPosition().toString() + " does not exist");
		}
		cell.setAgent(agent);
		agent.register(log);
		this.agents.add(agent);
	}

	public ArrayList<Agent> getAgents() {
		return this.agents;
	}

	public int getRows() {
		return this.m;
	}

	public int getCols() {
		return this.n;
	}

	public MapString getMapString() {
		return new MapString(this);
	}

	@Override
	public void register(BattleLog log) {
		this.log = log;
		for (Agent a : agents) {
			a.register(log);
		}
	}

}