package com.pipai.wf.battle.map;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GridPosition {
	
	public int x, y;
	
	public GridPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return "g(" + Integer.toString(this.x) + ", " + Integer.toString(this.y) + ")";
	}
	
	@Override
	public boolean equals(Object anObject) {
		if (anObject instanceof GridPosition) {
			GridPosition o = (GridPosition) anObject;
			return this.x == o.x && this.y == o.y;
		} else {
			return this == anObject;
		}
	}
	
	@Override
    public int hashCode() {
    	return new HashCodeBuilder(17, 31).append(x).append(y).toHashCode();
	}

}