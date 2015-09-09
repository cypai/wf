package com.pipai.wf.battle.vision;

import java.util.ArrayList;

import com.pipai.wf.battle.map.GridPosition;

public class VisionMatrix {

	private ArrayList<Long> matrix;
	private GridPosition center;
	private int radius;

	public VisionMatrix(GridPosition center, int radius) {
		// Longs are 64-bits long so we can only handle radii <= 31, though that should be fine
		matrix = new ArrayList<Long>();
		for (int i = 0; i < (1 + 2 * radius); i++) {
			matrix.add(new Long(0));
		}
		this.center = center;
		this.radius = radius;
	}

	public boolean isUnknownStatus(GridPosition tile) {
		int relativeX = tile.x - center.x;
		int relativeY = tile.y - center.y;
		long xBitMask = 1 << (radius - relativeX);
		return (matrix.get(relativeY) & xBitMask) > 0;
	}

	public void update(GridPosition newPos) {
		shiftMatrixHorizontally(newPos.x - center.x);
		shiftMatrixVertically(newPos.y - center.y);

	}

	private void shiftMatrixHorizontally(int amount) {
		if (amount == 0) {
			return;
		}
		int unsignedAmt = Math.abs(amount);
		ArrayList<Long> newMatrix = new ArrayList<Long>();
		if (amount > 0) {
			for (Long l : matrix) {
				newMatrix.add(l >> unsignedAmt);
			}
		} else {
			for (Long l : matrix) {
				newMatrix.add(l << unsignedAmt);
			}
		}
		matrix = newMatrix;
	}

	private void shiftMatrixVertically(int amount) {
		if (amount == 0) {
			return;
		}
		if (amount > 0) {
			for (int i = 0; i < amount; i++) {
				matrix.remove(0);
				matrix.add(new Long(0));
			}
		} else {
			for (int i = 0; i < amount; i++) {
				matrix.remove(matrix.size() - 1);
				matrix.add(0, new Long(0));
			}
		}
	}

}
