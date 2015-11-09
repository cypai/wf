package com.pipai.wf.battle.map;

import java.util.ArrayList;

import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.exception.BadStateStringException;

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
	private ArrayList<GridPosition> solidPosList;
	private ArrayList<AgentState> agentStateList;

	public MapString(String mapString) throws BadStateStringException {
		header = mapString.substring(0, mapString.indexOf("\n"));
		map = mapString.substring(mapString.indexOf("\n") + 1);
		String dimensions[] = header.split(" ");
		m = Integer.parseInt(dimensions[0]);
		n = Integer.parseInt(dimensions[1]);
		parse();
	}

	public MapString(int m, int n, String mapOnlyString) throws BadStateStringException {
		header = Integer.toString(m) + " " + Integer.toString(n);
		map = mapOnlyString;
		this.m = m;
		this.n = n;
		parse();
	}

	public MapString(BattleMap map) {
		m = map.getRows();
		n = map.getCols();
		header = Integer.toString(map.getRows()) + " " + Integer.toString(map.getCols());
		this.map = "";
		for (int x = 0; x < map.getCols(); x++) {
			for (int y = 0; y < map.getRows(); y++) {
				BattleMapCell cell = map.getCell(new GridPosition(x, y));
				if (cell.hasTileSightBlocker()) {
					this.map += "s " + Integer.toString(x) + " " + Integer.toString(y) + "\n";
				} else if (cell.hasAgent()) {
					this.map += "a " + Integer.toString(x) + " " + Integer.toString(y) + "\n";
				}
			}
		}
	}

	private void parse() throws BadStateStringException {
		solidPosList = new ArrayList<GridPosition>();
		agentStateList = new ArrayList<AgentState>();
		String lines[] = map.split("\n");
		for (String line : lines) {
			String params[] = line.split(" ");
			String type = params[0];
			if (type.equals("s")) {
				solidPosList.add(new GridPosition(Integer.parseInt(params[1]), Integer.parseInt(params[2])));
			} else {
				throw new BadStateStringException("Unknown line type");
			}
		}
	}

	public int getRows() {
		return m;
	}

	public int getCols() {
		return n;
	}

	public ArrayList<GridPosition> getSolidPositions() {
		return solidPosList;
	}

	public ArrayList<AgentState> getAgentStates() {
		return agentStateList;
	}

	@Override
	public String toString() {
		return header + "\n" + map;
	}

}