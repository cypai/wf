package com.pipai.wf.battle.weapon;

import com.pipai.wf.battle.BattleConfiguration;

public class InnateCasting extends SpellWeapon {

	public InnateCasting(BattleConfiguration config) {
		super(config);
	}

	@Override
	public boolean needsAmmunition() {
		return false;
	}

	@Override
	public int baseAmmoCapacity() {
		return 1;
	}

	@Override
	public String name() {
		return "Casting";
	}

}
