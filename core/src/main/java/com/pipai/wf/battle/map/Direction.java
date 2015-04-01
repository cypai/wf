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
	
}
