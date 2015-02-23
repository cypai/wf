package com.pipai.wf.battle.map;

public class GridPosition {
	
	public int x, y;
	
	public GridPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return "g(" + Integer.toString(this.x) + ", " + Integer.toString(this.y) + ")";
	}
}