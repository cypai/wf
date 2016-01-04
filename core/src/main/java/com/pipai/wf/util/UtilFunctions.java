package com.pipai.wf.util;

import com.pipai.wf.battle.map.GridPosition;

public final class UtilFunctions {

	public static boolean isInCircle(float centerX, float centerY, float radius, float x, float y) {
		return Math.pow(x - centerX, 2.0) + Math.pow(y - centerY, 2.0) <= radius * radius;
	}

	public static boolean isInRectangle(float blX, float blY, float width, float height, float x, float y) {
		return x >= blX && x <= blX + width && y >= blY && y <= blY + height;
	}

	public static boolean isInBoundingBox(float centerX, float centerY, float width, float height, float x, float y) {
		return isInRectangle(centerX - width / 2, centerY - height / 2, width, height, x, y);
	}

	public static int clamp(int min, int max, int value) {
		if (value > max) {
			return max;
		} else if (value < min) {
			return min;
		}
		return value;
	}

	public static float gridPositionDistance(GridPosition pos1, GridPosition pos2) {
		return (float) Math.sqrt(Math.pow(pos1.getX() - pos2.getX(), 2) + Math.pow(pos1.getY() - pos2.getY(), 2));
	}

	private UtilFunctions() {
		// Utility class should not have a public constructor
	}

}
