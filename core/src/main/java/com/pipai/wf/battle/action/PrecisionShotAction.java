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

public class PrecisionShotAction extends TargetedWithAccuracyAction {

	public PrecisionShotAction(Agent performerAgent, Agent targetAgent) {
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
		p.add(new PercentageModifier("Defense", -target.getDefense(a)));
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
		p.add(new PercentageModifier("Precision Shot", 30));
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
		DamageResult adjustedResult = new DamageResult(result.hit, result.crit, result.damage + (result.hit ? 1 : 0), result.damageReduction);
		target.takeDamage(result.damage);
		a.setAP(0);
		log(BattleEvent.rangedWeaponAttackEvent(a, target, w, adjustedResult));
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

	@Override
	public String name() {
		return "Precision Shot";
	}

	@Override
	public String description() {
		return "Fires a shot that has +1 extra damage and +30% critical chance.";
	}

}
