package com.pipai.wf.battle.map;

import java.util.ArrayList;

public enum Direction {
	N, S, E, W;
	
	public static ArrayList<Direction> getAllDirections() {
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
		}
		return l;
	}
	
}
