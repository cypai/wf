package com.pipai.wf.util;

public class UtilFunctions {
	
	public static int clamp(int min, int max, int value) {
		if (value > max) {
			return max;
		} else if (value < min) {
			return min;
		}
		return value;
	}
	
}
