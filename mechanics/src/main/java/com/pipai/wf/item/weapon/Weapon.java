package com.pipai.wf.item.weapon;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.action.RangedWeaponAttackAction;
import com.pipai.wf.battle.action.ReloadAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.item.Item;
import com.pipai.wf.unit.ability.Ability;
import com.pipai.wf.unit.ability.AbilityList;
import com.sun.javafx.util.Utils;

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

	public abstract int baseAmmoCapacity();

	public void expendAmmo(int amount) {
		currentAmmo -= amount;
	}

	public int getCurrentAmmo() {
		return currentAmmo;
	}

	public void setCurrentAmmo(int ammo) {
		currentAmmo = Utils.clamp(0, ammo, baseAmmoCapacity());
	}

	public void reload() {
		currentAmmo = baseAmmoCapacity();
	}

	@JsonIgnore
	public final AbilityList getGrantedAbilities() {
		return grantedAbilities;
	}

	public abstract boolean hasFlag(WeaponFlag flag);

	@Override
	public List<Action> getAvailableActions(BattleController controller, Agent agent) {
		ArrayList<Action> actions = new ArrayList<>();
		if (agent.getInventory().contains(this)) {
			boolean canFire = currentAmmo > 0 || !hasFlag(WeaponFlag.NEEDS_AMMUNITION);
			if (canFire) {
				actions.add(new RangedWeaponAttackAction(controller, agent, this));
				if (hasFlag(WeaponFlag.OVERWATCH)) {
					OverwatchAction owAction = new OverwatchAction(controller, agent, this,
							new RangedWeaponAttackAction(controller, agent, this));
					actions.add(owAction);
				}
			}
			boolean needsReload = hasFlag(WeaponFlag.NEEDS_AMMUNITION) && currentAmmo < baseAmmoCapacity();
			if (needsReload) {
				actions.add(new ReloadAction(controller, agent, this));
			}
		}
		return actions;
	}

	@Override
	public abstract Weapon copyAsNew();

	@Override
	public Weapon copy() {
		Weapon copy = copyAsNew();
		copy.setCurrentAmmo(getCurrentAmmo());
		return copy;
	}

}
