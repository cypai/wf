package com.pipai.wf.util;

import java.util.Objects;

public class GridPosition {

	private final int x, y;

	public GridPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public GridPosition(GridPosition other) {
		x = other.x;
		y = other.y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "g(" + Integer.toString(x) + ", " + Integer.toString(y) + ")";
	}

	@Override
	public boolean equals(Object anObject) {
		if (anObject instanceof GridPosition) {
			GridPosition o = (GridPosition) anObject;
			return x == o.x && y == o.y;
		} else {
			return this == anObject;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	public double distance(GridPosition other) {
		return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
	}

	// SUPPRESS CHECKSTYLE CyclomaticComplexity Can't get much clearer
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
