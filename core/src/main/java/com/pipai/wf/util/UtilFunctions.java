package com.pipai.wf.util;

import java.util.Random;

public class UtilFunctions {

	public static final Random rng = new Random();
	
	/*
	 * Generates a random integer between min and max inclusive.
	 */
	public static int randInt(int min, int max) {
		return rng.nextInt(max - min + 1) + min;
	}
	
	public static boolean isInCircle(float centerX, float centerY, float radius, float x, float y) {
		return Math.pow(x - centerX, 2.0) + Math.pow(y - centerY, 2.0) <= radius*radius;
	}
	
	public static boolean isInRectangle(float blX, float blY, float width, float height, float x, float y) {
		return x >= blX && x <= blX + width && y >= blY && y <= blY + height;
	}
	
	public static boolean isInBoundingBox(float centerX, float centerY, float width, float height, float x, float y) {
		return isInRectangle(centerX - width/2, centerY - height/2, width, height, x, y);
	}
	
	public static int clamp(int min, int max, int value) {
		if (value > max) {
			return max;
		} else if (value < min) {
			return min;
		}
		return value;
	}
	
}
