package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.damage.PercentageModifierList;
import com.pipai.wf.battle.damage.WeaponDamageFunction;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.util.UtilFunctions;

public class RangedWeaponAttackAction extends TargetedWithAccuracyActionOWCapable {

	public RangedWeaponAttackAction(BattleController controller, Agent performerAgent, Agent targetAgent) {
		super(controller, performerAgent, targetAgent);
	}

	@Override
	public int toHit() {
		int total_aim = getHitCalculation().total();
		return UtilFunctions.clamp(1, 100, total_aim);
	}

	@Override
	public int toCrit() {
		int crit_prob = getCritCalculation().total();
		return UtilFunctions.clamp(1, 100, crit_prob);
	}

	@Override
	public PercentageModifierList getHitCalculation() {
		Agent a = getPerformer();
		Agent target = getTarget();
		return getTargetedActionCalculator().baseHitCalculation(getBattleMap(), a, target);
	}

	@Override
	public PercentageModifierList getCritCalculation() {
		Agent a = getPerformer();
		Agent target = getTarget();
		return getTargetedActionCalculator().baseCritCalculation(getBattleMap(), a, target);
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		Agent a = getPerformer();
		Agent target = getTarget();
		Weapon w = a.getCurrentWeapon();
		if (w.needsAmmunition() && w.currentAmmo() == 0) {
			throw new IllegalActionException("Not enough ammo to fire " + w.getName());
		}
		DamageResult result = getDamageCalculator().rollDamageGeneral(this, new WeaponDamageFunction(w), 0);
		a.setAP(0);
		getDamageDealer().doDamage(result, target);
		log(BattleEvent.rangedWeaponAttackEvent(a, target, w, result));
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

	@Override
	public void performOnOverwatch(BattleEvent parent) throws IllegalActionException {
		Agent a = getPerformer();
		Agent target = getTarget();
		Weapon w = a.getCurrentWeapon();
		if (w.needsAmmunition() && w.currentAmmo() == 0) {
			throw new IllegalActionException("Not enough ammo to fire " + w.getName());
		}
		DamageResult result = getDamageCalculator().rollDamageGeneral(this, new WeaponDamageFunction(w), 1);
		a.setAP(0);
		getDamageDealer().doDamage(result, target);
		parent.addChainEvent(BattleEvent.overwatchActivationEvent(a, target, this, target.getPosition(), result));
	}

	@Override
	public String getName() {
		return "Attack";
	}

	@Override
	public String getDescription() {
		Weapon weapon = getPerformer().getCurrentWeapon();
		if (weapon.needsAmmunition() && weapon.currentAmmo() <= 0) {
			return "Not enough ammunition";
		} else {
			return "Attack with the selected ranged weapon";
		}
	}

}
