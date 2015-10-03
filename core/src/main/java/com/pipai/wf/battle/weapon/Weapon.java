package com.pipai.wf.battle.weapon;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.misc.HasName;
import com.pipai.wf.unit.ability.Ability;
import com.pipai.wf.unit.ability.AbilityList;

public abstract class Weapon implements HasName {

	private int currentAmmo;
	private AbilityList grantedAbilities;
	private BattleConfiguration config;

	public Weapon(int initialAmmo, BattleConfiguration config) {
		currentAmmo = initialAmmo;
		grantedAbilities = new AbilityList();
		this.config = config;
	}

	protected final void addGrantedAbility(Ability a) {
		grantedAbilities.add(a);
	}

	public abstract int flatAimModifier();

	public abstract int rangeAimModifier(float distance);

	public abstract int flatCritProbabilityModifier();

	public abstract int rangeCritModifier(float distance);

	public abstract int minBaseDamage();

	public abstract int maxBaseDamage();

	public abstract boolean needsAmmunition();

	public abstract int baseAmmoCapacity();

	public void expendAmmo(int amount) {
		currentAmmo -= amount;
	}

	public int currentAmmo() {
		return currentAmmo;
	}

	public void reload() {
		currentAmmo = baseAmmoCapacity();
	}

	public final AbilityList getGrantedAbilities() {
		return grantedAbilities;
	}

	public final BattleConfiguration getConfig() {
		return config;
	}

}
