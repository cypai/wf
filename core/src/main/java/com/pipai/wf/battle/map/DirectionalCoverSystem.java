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
	
	public static ArrayList<Direction> getNeededCoverDirections(GridPosition pos, GridPosition attackPos) {
		ArrayList<Direction> l = new ArrayList<Direction>();
		if (pos.x == attackPos.x) {
			if (attackPos.y > pos.y) {
				l.add(Direction.N);
			} else {
				l.add(Direction.S);
			}
		} else if (pos.y == attackPos.y) {
			if (attackPos.x > pos.x) {
				l.add(Direction.E);
			} else {
				l.add(Direction.W);
			}
		} else {
			if (attackPos.x > pos.x && attackPos.y > pos.y) {
				l.add(Direction.N);
				l.add(Direction.E);
			} else if (attackPos.x > pos.x && attackPos.y < pos.y) {
				l.add(Direction.S);
				l.add(Direction.E);
			} else if (attackPos.x < pos.x && attackPos.y > pos.y) {
				l.add(Direction.N);
				l.add(Direction.W);
			} else {
				l.add(Direction.S);
				l.add(Direction.E);
			}
		}
		return l;
	}
	
	public static boolean isOpen(BattleMap map, GridPosition pos) {
		return getCoverDirections(map, pos).size() == 0;
	}
	
	public static boolean isFlankedBy(BattleMap map, GridPosition pos, GridPosition flanker) {
		ArrayList<Direction> coveredDirs = getCoverDirections(map, pos);
		ArrayList<Direction> neededDirs = getNeededCoverDirections(pos, flanker);
		for (Direction needed : neededDirs) {
			if (coveredDirs.contains(needed)) {
				return false;
			}
		}
		return true;
	}
	
}