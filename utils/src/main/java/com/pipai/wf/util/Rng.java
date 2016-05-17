package com.pipai.wf.util;

import java.util.Random;

public class Rng {

	private final Random rng;

	public Rng() {
		rng = new Random();
	}

	public int nextInt() {
		return rng.nextInt();
	}

	public int nextInt(int bound) {
		return rng.nextInt(bound);
	}

	/*
	 * Generates a random integer between min and max inclusive.
	 */
	public int randInt(int min, int max) {
		return rng.nextInt(max - min + 1) + min;
	}

}
