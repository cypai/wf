package com.pipai.wf.battle.map;

public enum CoverType {
	
	FULL(40), HALF(30), NONE(0);
	
	private final int defense;
	
	private CoverType(int defense) {
		this.defense = defense;
	}
	
	public int getDefense() {
		return defense;
	}
	
}