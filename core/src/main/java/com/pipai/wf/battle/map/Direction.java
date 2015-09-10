package com.pipai.wf.battle.map;

import java.util.ArrayList;

public enum Direction {
	N(true), S(true), E(true), W(true), NW(false), NE(false), SW(false), SE(false);

	public final boolean isCardinal;

	private Direction(boolean isCardinal) {
		this.isCardinal = isCardinal;
	}

	public static ArrayList<Direction> getCardinalDirections() {
		ArrayList<Direction> l = new ArrayList<Direction>();
		l.add(N);
		l.add(S);
		l.add(E);
		l.add(W);
		return l;
	}

	public static ArrayList<Direction> getPerpendicular(Direction d) {
		ArrayList<Direction> l = new ArrayList<Direction>();
		switch (d) {
		case N:
		case S:
			l.add(E);
			l.add(W);
			break;
		case E:
		case W:
			l.add(N);
			l.add(S);
			break;
		default:
			throw new IllegalArgumentException("getPerpendicular not implemented for non-cardinal directions");
		}
		return l;
	}

}
