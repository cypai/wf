package com.pipai.wf.battle.map;

public class FullCoverIndestructibleObject extends EnvironmentObject {

	public FullCoverIndestructibleObject() {
		super(1);
	}
	
	public void takeDamage(int dmg) {
		// Do nothing
	}
	
	@Override
	public CoverType getCoverType() {
		return CoverType.FULL;
	}
	
}
