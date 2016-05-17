package com.pipai.wf.battle.action.component;

import com.pipai.wf.item.weapon.Weapon;

public interface HasWeaponComponent extends WeaponComponent {

	WeaponComponent getWeaponComponent();

	@Override
	default Weapon getWeapon() {
		return getWeaponComponent().getWeapon();
	}

	@Override
	default void setWeapon(Weapon weapon) {
		getWeaponComponent().setWeapon(weapon);
	}

}
