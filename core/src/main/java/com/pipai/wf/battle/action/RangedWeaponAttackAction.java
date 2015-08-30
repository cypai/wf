package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.DamageCalculator;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.damage.PercentageModifier;
import com.pipai.wf.battle.damage.PercentageModifierList;
import com.pipai.wf.battle.damage.WeaponDamageFunction;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.util.UtilFunctions;

public class RangedWeaponAttackAction extends TargetedWithAccuracyActionOWCapable {

	public RangedWeaponAttackAction(Agent performerAgent, Agent targetAgent) {
		super(performerAgent, targetAgent);
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
		Weapon weapon = a.getCurrentWeapon();
		PercentageModifierList p = new PercentageModifierList();
		p.add(new PercentageModifier("Aim", a.getBaseAim()));
		p.add(new PercentageModifier("Weapon Aim", weapon.flatAimModifier()));
		p.add(new PercentageModifier("Range", weapon.rangeAimModifier(a.getDistanceFrom(target))));
		p.add(new PercentageModifier("Defense", -target.getDefense(a.getPosition())));
		return p;
	}

	@Override
	public PercentageModifierList getCritCalculation() {
		Agent a = getPerformer();
		Agent target = getTarget();
		Weapon weapon = a.getCurrentWeapon();
		PercentageModifierList p = new PercentageModifierList();
		p.add(new PercentageModifier("Weapon Base", weapon.flatCritProbabilityModifier()));
		p.add(new PercentageModifier("Range", weapon.rangeCritModifier(a.getDistanceFrom(target))));
		if (target.isFlankedBy(a)) {
			p.add(new PercentageModifier("No Cover", 30));
		}
		return p;
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		Agent a = getPerformer();
		Agent target = getTarget();
		Weapon w = a.getCurrentWeapon();
		if (w.needsAmmunition() && w.currentAmmo() == 0) {
			throw new IllegalActionException("Not enough ammo to fire " + w.name());
		}
		DamageResult result = DamageCalculator.rollDamageGeneral(this, new WeaponDamageFunction(w), 0);
		a.setAP(0);
		target.takeDamage(result.damage);
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
			throw new IllegalActionException("Not enough ammo to fire " + w.name());
		}
		DamageResult result = DamageCalculator.rollDamageGeneral(this, new WeaponDamageFunction(w), 1);
		a.setAP(0);
		target.takeDamage(result.damage);
		parent.addChainEvent(BattleEvent.overwatchActivationEvent(a, target, this, target.getPosition(), result));
	}

	@Override
	public String name() {
		return "Attack";
	}

	@Override
	public String description() {
		Weapon weapon = getPerformer().getCurrentWeapon();
		if (weapon.needsAmmunition() && weapon.currentAmmo() <= 0) {
			return "Not enough ammunition";
		} else {
			return "Attack with the selected ranged weapon";
		}
	}

}
