package com.pipai.wf.battle.damage;

import com.pipai.wf.battle.weapon.Weapon;

public class WeaponDamageFunction extends DamageFunction {
	
	private Weapon w;
	
	public WeaponDamageFunction(Weapon w) {
		this.w = w;
	}
	
	@Override
	public int rollForDamage() {
		return w.rollForDamage();
	}
	
}
