package com.pipai.wf.battle.vision;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.util.GridPosition;

public class LineOfSightCalculator {

	public static boolean checkTileSightBlockersInList(BattleMap map, ArrayList<GridPosition> list) {
		for (GridPosition pos : list) {
			if (map.getCell(pos).hasTileSightBlocker()) {
				return false;
			}
		}
		return true;
	}

	public static boolean checkCornerPairsForSightBlockers(BattleMap map, ArrayList<Supercover.CornerPair> list) {
		for (Supercover.CornerPair pair : list) {
			if (map.getCell(pair.a).hasTileSightBlocker() && map.getCell(pair.b).hasTileSightBlocker()) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Using the centers/corners to determine line of sight between two
	 * GridPositions
	 */
	public static boolean lineOfSight(BattleMap map, GridPosition a, GridPosition b) {
		Supercover sc = supercover(gridCenter(a), gridCenter(b));
		if (checkTileSightBlockersInList(map, sc.supercover)) {
			if (checkCornerPairsForSightBlockers(map, sc.corners)) {
				return true;
			}
		}
		return false;
	}

	protected static Supercover supercover(GridPosition a, GridPosition b) {
		return supercover(gridCenter(a), gridCenter(b));
	}

	private static Supercover supercover(Vector2 a, Vector2 b) {
		// CHECKSTYLE:OFF Trailing Comments actually help readability here
		Supercover points = new Supercover();
		int i; // loop counter
		int ystep, xstep; // the step on y and x axis
		int error; // the error accumulated during the increment
		int errorprev; // *vision the previous value of the error variable
		int y = (int) a.y, x = (int) a.x; // the line points
		int ddy, ddx; // compulsory variables: the double values of dy and dx
		int dx = (int) b.x - (int) a.x;
		int dy = (int) b.y - (int) a.y;
		// NB the last point can't be here, because of its previous point (which has to be verified)
		if (dy < 0) {
			ystep = -1;
			dy = -dy;
		} else {
			ystep = 1;
		}
		if (dx < 0) {
			xstep = -1;
			dx = -dx;
		} else {
			xstep = 1;
		}
		// work with double values for full precision
		ddy = 2 * dy;
		ddx = 2 * dx;
		if (ddx >= ddy) { // first octant (0 <= slope <= 1)
			// compulsory initialization (even for errorprev, needed when dx==dy)
			error = dx; // start in the middle of the square
			errorprev = error;
			for (i = 0; i < dx; i++) { // do not use the first point (already done)
				x += xstep;
				error += ddy;
				if (error > ddx) { // increment y if AFTER the middle ( > )
					y += ystep;
					error -= ddx;
					// three cases (octant == right->right-top for directions below):
					if (error + errorprev < ddx) {
						points.add(new GridPosition(x, y - ystep));
					} else if (error + errorprev > ddx) {
						points.add(new GridPosition(x - xstep, y));
					} else { // corner: bottom and left squares also
						points.addCornerPair(new GridPosition(x, y - ystep), new GridPosition(x - xstep, y));
					}
				}
				points.add(new GridPosition(x, y));
				errorprev = error;
			}
		} else { // the same as above
			error = dy;
			errorprev = error;
			for (i = 0; i < dy; i++) {
				y += ystep;
				error += ddx;
				if (error > ddy) {
					x += xstep;
					error -= ddy;
					if (error + errorprev < ddy) {
						points.add(new GridPosition(x - xstep, y));
					} else if (error + errorprev > ddy) {
						points.add(new GridPosition(x, y - ystep));
					} else {
						points.addCornerPair(new GridPosition(x - xstep, y), new GridPosition(x, y - ystep));
					}
				}
				points.add(new GridPosition(x, y));
				errorprev = error;
			}
		}
		points.remove(new GridPosition((int) b.x, (int) b.y));
		// CHECKSTYLE:ON
		return points;
	}

	private static Vector2 gridCenter(GridPosition pos) {
		return new Vector2(pos.getX() + 0.5f, pos.getY() + 0.5f);
	}

	protected static class Supercover {

		private ArrayList<GridPosition> supercover;
		private ArrayList<CornerPair> corners;

		public Supercover() {
			supercover = new ArrayList<GridPosition>();
			corners = new ArrayList<CornerPair>();
		}

		public List<GridPosition> getSupercover() {
			return supercover;
		}

		public void remove(GridPosition pos) {
			supercover.remove(pos);
		}

		public void add(GridPosition pos) {
			supercover.add(pos);
		}

		public void addCornerPair(GridPosition a, GridPosition b) {
			corners.add(new CornerPair(a, b));
		}

		public class CornerPair {
			private GridPosition a, b;

			CornerPair(GridPosition a, GridPosition b) {
				this.a = a;
				this.b = b;
			}
		}

	}

}
