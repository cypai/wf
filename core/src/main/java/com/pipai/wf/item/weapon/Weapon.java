package com.pipai.wf.item.weapon;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
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

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class Weapon extends Item {

	private static final RangedWeaponAttackAction RANGED_ATTACK_ACTION = new RangedWeaponAttackAction();
	private static final ReloadAction RELOAD_ACTION = new ReloadAction();
	private static final OverwatchAction OVERWATCH_ACTION = new OverwatchAction();

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
				RANGED_ATTACK_ACTION.setBattleController(controller);
				RANGED_ATTACK_ACTION.setPerformer(agent);
				actions.add(RANGED_ATTACK_ACTION);
				if (hasFlag(WeaponFlag.OVERWATCH)) {
					OVERWATCH_ACTION.setBattleController(controller);
					OVERWATCH_ACTION.setPerformer(agent);
					OVERWATCH_ACTION.setOverwatchAction(new RangedWeaponAttackAction(controller, agent));
				}
			}
			boolean needsReload = hasFlag(WeaponFlag.NEEDS_AMMUNITION) && currentAmmo < baseAmmoCapacity();
			if (needsReload) {
				RELOAD_ACTION.setBattleController(controller);
				RELOAD_ACTION.setPerformer(agent);
				RELOAD_ACTION.setWeapon(this);
				actions.add(RELOAD_ACTION);
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
