package com.pipai.wf.battle.map;

import java.util.ArrayList;

/*
 * A MapString is the interface between the string representation of a map and the BattleMap
 * Layout:
 * m n		//Header for dimensions
 * s 1 1	//Position of cell solids
 * a 2 0	//Position of agent at (2, 0)
 */

public class MapString {
	
	private String header;
	private String map;
	private int m, n;
	private ArrayList<Position> solidPosList, agentPosList;
	
	public MapString(String mapString) {
		this.header = mapString.substring(0, mapString.indexOf("\n"));
		this.map = mapString.substring(mapString.indexOf("\n") + 1);
		String dimensions[] = this.header.split(" ");
		this.m = Integer.parseInt(dimensions[0]);
		this.n = Integer.parseInt(dimensions[1]);
		this.parseForPositions();
	}
	
	public MapString(int m, int n, String mapOnlyString) {
		this.header = Integer.toString(m) + " " + Integer.toString(n);
		this.map = mapOnlyString;
		this.m = m;
		this.n = n;
		this.parseForPositions();
	}
	
	public MapString(BattleMap map) {
		this.m = map.getRows();
		this.n = map.getCols();
		this.header = Integer.toString(map.getRows()) + " " + Integer.toString(map.getCols());
		this.map = "";
		for (int x = 0; x < map.getCols(); x++) {
			for (int y = 0; y < map.getRows(); y++) {
				BattleMapCell cell = map.getCell(new Position(x, y));
				if (cell.isSolid()) {
					this.map += "s " + Integer.toString(x) + " " + Integer.toString(y) + "\n";
				} else if (cell.hasAgent()) {
					this.map += "a " + Integer.toString(x) + " " + Integer.toString(y) + "\n";
				}
			}
		}
	}
	
	private void parseForPositions() {
		this.solidPosList = new ArrayList<Position>();
		this.agentPosList = new ArrayList<Position>();
		String lines[] = this.map.split("\n");
		for (String line : lines) {
			String params[] = line.split(" ");
			String type = params[0];
			if (type.equals("s")) {
				this.solidPosList.add(new Position(Integer.parseInt(params[1]), Integer.parseInt(params[2])));
			} else if (type.equals("a")) {
				this.agentPosList.add(new Position(Integer.parseInt(params[1]), Integer.parseInt(params[2])));
			}
		}
	}
	
	public int getRows() { return this.m; }
	public int getCols() { return this.n; }
	
	public ArrayList<Position> getSolidPositions() {
		return this.solidPosList;
	}
	
	public ArrayList<Position> getAgentPositions() {
		return this.agentPosList;
	}
	
	public String toString() {
		return this.header + "\n" + this.map;
	}
	
}