package com.pipai.wf.battle.map;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GridPosition {

	public int x, y;

	public GridPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public GridPosition(GridPosition other) {
		this.x = other.x;
		this.y = other.y;
	}

	@Override
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

	public double distance(GridPosition other) {
		return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
	}

	public Direction directionTo(GridPosition other) {
		if (x == other.x) {
			if (other.y > y) {
				return Direction.N;
			} else {
				return Direction.S;
			}
		} else if (y == other.y) {
			if (other.x > x) {
				return Direction.E;
			} else {
				return Direction.W;
			}
		} else {
			if (other.x > x && other.y > y) {
				return Direction.NE;
			} else if (other.x > x && other.y < y) {
				return Direction.SE;
			} else if (other.x < x && other.y > y) {
				return Direction.NW;
			} else {
				return Direction.SW;
			}
		}
	}

}