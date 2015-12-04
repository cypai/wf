package com.pipai.wf.battle.map;

import java.util.ArrayList;

public enum Direction {
	N(true), S(true), E(true), W(true), NW(false), NE(false), SW(false), SE(false);

	private final boolean cardinal;

	Direction(boolean isCardinal) {
		this.cardinal = isCardinal;
	}

	public boolean isCardinal() {
		return cardinal;
	}

	public static ArrayList<Direction> getCardinalDirections() {
		ArrayList<Direction> l = new ArrayList<>();
		l.add(N);
		l.add(S);
		l.add(E);
		l.add(W);
		return l;
	}

	public static ArrayList<Direction> getPerpendicular(Direction d) {
		ArrayList<Direction> l = new ArrayList<>();
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
