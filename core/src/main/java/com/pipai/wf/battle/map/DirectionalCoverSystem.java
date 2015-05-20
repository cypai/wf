package com.pipai.wf.battle.map;

import java.util.ArrayList;

public class DirectionalCoverSystem {
	
	public static CoverType getDirectionalCover(BattleMap map, GridPosition pos, Direction dir) {
		BattleMapCell cell = map.getCellInDirection(pos, dir);
		if (cell == null) {
			return CoverType.NONE;
		}
		EnvironmentObject env = cell.getTileEnvironmentObject();
		if (env != null) {
			return env.getCoverType();
		} else {
			return CoverType.NONE;
		}
	}
	
	public static CoverType getCover(BattleMap map, GridPosition pos) {
		CoverType best = CoverType.NONE;
		for (Direction d : Direction.getAllDirections()) {
			if (getDirectionalCover(map, pos, d) == CoverType.FULL) {
				return CoverType.FULL;
			}
			if (getDirectionalCover(map, pos, d) == CoverType.HALF) {
				best = CoverType.HALF;
			}
		}
		return best;
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