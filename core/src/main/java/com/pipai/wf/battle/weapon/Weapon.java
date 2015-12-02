package com.pipai.wf.battle.weapon;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.item.Item;
import com.pipai.wf.unit.ability.Ability;
import com.pipai.wf.unit.ability.AbilityList;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class Weapon extends Item {

	private int currentAmmo;
	private AbilityList grantedAbilities;

	public Weapon(int initialAmmo) {
		currentAmmo = initialAmmo;
		grantedAbilities = new AbilityList();
	}

	protected final void addGrantedAbility(Ability a) {
		grantedAbilities.add(a);
	}

	public abstract int flatAimModifier();

	public abstract int rangeAimModifier(float distance, BattleConfiguration config);

	public abstract int flatCritProbabilityModifier();

	public abstract int rangeCritModifier(float distance, BattleConfiguration config);

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

}
