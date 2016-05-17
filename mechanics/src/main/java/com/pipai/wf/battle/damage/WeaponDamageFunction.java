package com.pipai.wf.battle.damage;

import com.pipai.wf.item.weapon.Weapon;
import com.pipai.wf.util.Rng;

public class WeaponDamageFunction extends DamageFunction {

	private Weapon w;

	public WeaponDamageFunction(Weapon w) {
		this.w = w;
	}

	@Override
	public int rollForDamage(Rng rng) {
		return rng.randInt(w.minBaseDamage(), w.maxBaseDamage());
	}

}
