package com.pipai.wf.battle.map;

public class HalfCoverIndestructibleObject extends EnvironmentObject {

	public HalfCoverIndestructibleObject() {
		super(1);
	}
	
	public void takeDamage(int dmg) {
		// Do nothing
	}
	
	@Override
	public CoverType getCoverType() {
		return CoverType.HALF;
	}
	
}
