package com.pipai.wf.battle.map;

public class Position {
	
	public int x, y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return "(" + Integer.toString(this.x) + ", " + Integer.toString(this.y) + ")";
	}
}