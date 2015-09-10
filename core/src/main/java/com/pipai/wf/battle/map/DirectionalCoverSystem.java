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
		for (Direction d : Direction.getCardinalDirections()) {
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
		for (Direction d : Direction.getCardinalDirections()) {
			if (getDirectionalCover(map, pos, d) != CoverType.NONE) {
				l.add(d);
			}
		}
		return l;
	}

	public static ArrayList<Direction> getNeededCoverDirections(GridPosition pos, GridPosition attackPos) {
		ArrayList<Direction> l = new ArrayList<Direction>();
		Direction d = pos.directionTo(attackPos);
		if (d.isCardinal) {
			l.add(d);
			return l;
		} else {
			switch (d) {
			case NW:
				l.add(Direction.N);
				l.add(Direction.W);
				break;
			case SW:
				l.add(Direction.S);
				l.add(Direction.W);
				break;
			case NE:
				l.add(Direction.N);
				l.add(Direction.E);
				break;
			case SE:
				l.add(Direction.S);
				l.add(Direction.E);
				break;
			default:
				throw new RuntimeException(d.toString() + " was unexpectedly sent to switch statement");
			}
			return l;
		}
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

	public static CoverType getBestCoverAgainstAttack(BattleMap map, GridPosition pos, GridPosition attacker) {
		ArrayList<Direction> neededDirs = getNeededCoverDirections(pos, attacker);
		CoverType bestCover = CoverType.NONE;
		for (Direction neededDir : neededDirs) {
			CoverType cover = getDirectionalCover(map, pos, neededDir);
			if (cover.getDefense() > bestCover.getDefense()) {
				bestCover = cover;
			}
		}
		return bestCover;
	}

}