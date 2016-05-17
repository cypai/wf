package com.pipai.wf.battle.action.component;

import com.pipai.wf.item.weapon.Weapon;

public class WeaponComponentImpl implements WeaponComponent {

	private Weapon weapon;

	@Override
	public Weapon getWeapon() {
		return weapon;
	}

	@Override
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

}
