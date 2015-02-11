package com.pipai.wf.battle;

import java.util.HashMap;

import com.pipai.wf.battle.BattleMapCell;

public class BattleMap {
	
	private int n, m;	// Size of the map
	private HashMap<String, BattleMapCell> cellMap;
	
	public BattleMap(int n, int m) {
		initializeMap(n, m);
	}
	
	public BattleMap(String mapString) {
		String lines[] = mapString.split("\n");
		boolean header = true;
		int y_top = 0;
		for (String line : lines) {
			if (header) {
				header = false;
				String dimensions[] = line.split(" ");
				this.initializeMap(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]));
			} else {
				String solids[] = line.split(" ");
				int x = 0;
				for (String solid : solids) {
					int y = this.m - 1 - y_top;
					if (solid.equals("1")) {
						this.getCell(x, y).setSolid(true);
					}
					x++;
				}
				y_top++;
			}
		}
	}
	
	/*
	 * Creates an empty map of size n x m
	 */
	private void initializeMap(int n, int m) {
		this.n = n;
		this.m = m;
		
		this.cellMap = new HashMap<String, BattleMapCell>();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				BattleMapCell cell = new BattleMapCell();
				this.cellMap.put(this.coordinatesToKey(i,j), cell);
				if (i > 0) {
					BattleMapCell west = this.getCell(i - 1, j);
					west.setNeighbor(cell, BattleMapCell.Direction.E);
					cell.setNeighbor(west, BattleMapCell.Direction.W);
				}
				if (j > 0) {
					BattleMapCell south = this.getCell(i, j - 1);
					south.setNeighbor(cell, BattleMapCell.Direction.N);
					cell.setNeighbor(south, BattleMapCell.Direction.S);
				}
			}
		}
	}
	
	public String coordinatesToKey(int x, int y) {
		return "(" + Integer.toString(x) + ", " + Integer.toString(y) + ")";
	}
	
	public BattleMapCell getCell(int x, int y) {
		return this.cellMap.get(this.coordinatesToKey(x, y));
	}
}