package com.pipai.wf.levelling;

public final class LevellingSystem {

	public static int levelForExp(int exp) {
		return Math.floorDiv(exp, 20) + 1;
	}

	public static int expToNextLevel(int exp) {
		return exp % 20;
	}

	private LevellingSystem() {
	}

}
