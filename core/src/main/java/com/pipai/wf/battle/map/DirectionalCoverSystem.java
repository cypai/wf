package com.pipai.wf.battle.map;

import java.util.ArrayList;

public class DirectionalCoverSystem {
	
	public static enum CoverType { FULL, HALF, NONE }
	
	public static CoverType getDirectionalCover(BattleMap map, GridPosition pos, Direction dir) {
		BattleMapCell cell = map.getCellInDirection(pos, dir);
		if (cell == null) {
			return CoverType.NONE;
		}
		if (cell.isSolid()) {
			return CoverType.FULL;
		}
		return CoverType.NONE;
	}
	
	public static boolean inCover(BattleMap map, GridPosition pos) {
		for (Direction d : Direction.getAllDirections()) {
			if (getDirectionalCover(map, pos, d) != CoverType.NONE) {
				return true;
			}
		}
		return false;
	}
	
	public static ArrayList<Direction> getCoverDirections(BattleMap map, GridPosition pos) {
		ArrayList<Direction> l = new ArrayList<Direction>();
		for (Direction d : Direction.getAllDirections()) {
			if (getDirectionalCover(map, pos, d) != CoverType.NONE) {
				l.add(d);
			}
		}
		return l;
	}
	
}