package com.pipai.wf.battle.map;

import java.util.HashMap;
import java.util.ArrayList;

import com.pipai.wf.battle.Agent;

public class BattleMap {
	
	private int m, n;	// Size of the map
	private HashMap<String, BattleMapCell> cellMap;
	private ArrayList<Agent> agents;
	
	public BattleMap(int m, int n) {
		this.agents = new ArrayList<Agent>();
		initializeMap(m, n);
	}
	
	public BattleMap(MapString mapString) {
		this.agents = new ArrayList<Agent>();
		this.m = mapString.getRows();
		this.n = mapString.getCols();
		initializeMap(this.m, this.n);
		for (GridPosition pos : mapString.getSolidPositions()) {
			//System.out.println(pos);
			this.getCell(pos).setSolid(true);
		}
		for (GridPosition pos : mapString.getAgentPositions()) {
			this.addAgentAtPos(pos, Agent.Team.PLAYER);
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
					BattleMapCell west = this.getCell(new GridPosition(i-1, j));
					west.setNeighbor(cell, BattleMapCell.Direction.E);
					cell.setNeighbor(west, BattleMapCell.Direction.W);
				}
				if (j > 0) {
					BattleMapCell south = this.getCell(new GridPosition(i, j-1));
					south.setNeighbor(cell, BattleMapCell.Direction.N);
					cell.setNeighbor(south, BattleMapCell.Direction.S);
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
	
	public Agent getAgentAtPos(GridPosition pos) {
		return this.getCell(pos).getAgent();
	}
	
	public void addAgentAtPos(GridPosition pos, Agent.Team team) {
		Agent agent = new Agent(this, team);
		this.getCell(pos).setAgent(agent);
		this.agents.add(agent);
	}
	
	public ArrayList<Agent> getAgents() {
		return this.agents;
	}
	
	public int getRows() { return this.m; }
	public int getCols() { return this.n; }
	
	public MapString getMapString() {
		return new MapString(this);
	}
	
}