package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.damage.PercentageModifier;
import com.pipai.wf.battle.damage.PercentageModifierList;
import com.pipai.wf.battle.damage.WeaponDamageFunction;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.battle.weapon.WeaponFlag;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.unit.ability.Ability;
import com.pipai.wf.unit.ability.PrecisionShotAbility;
import com.pipai.wf.util.UtilFunctions;

public class PrecisionShotAction extends TargetedWithAccuracyAction {

	public PrecisionShotAction(BattleController controller, Agent performerAgent, Agent targetAgent) {
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
		PercentageModifierList calc = getTargetedActionCalculator().baseCritCalculation(getBattleMap(), a, target);
		calc.add(new PercentageModifier("Precision Shot", 30));
		return calc;
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		Agent attacker = getPerformer();
		Ability ability = attacker.getAbility(PrecisionShotAbility.class);
		if (ability == null) {
			throw new IllegalActionException(attacker.getName() + "does not have Precision Shot ability");
		}
		Agent target = getTarget();
		Weapon w = attacker.getCurrentWeapon();
		if (!w.hasFlag(WeaponFlag.PRECISE_SHOT)) {
			throw new IllegalActionException(w.getName() + " cannot use Precise Shot");
		}
		if (w.hasFlag(WeaponFlag.NEEDS_AMMUNITION) && w.currentAmmo() == 0) {
			throw new IllegalActionException("Not enough ammo to fire " + w.getName());
		}
		DamageResult result = getDamageCalculator().rollDamageGeneral(this, new WeaponDamageFunction(w), 0);
		DamageResult adjustedResult = new DamageResult(result.isHit(), result.isCrit(),
				result.getDamage() + (result.isHit() ? 1 : 0), result.getDamageReduction());
		getDamageDealer().doDamage(adjustedResult, target);
		attacker.setAP(0);
		ability.setCooldown(2);
		logBattleEvent(BattleEvent.rangedWeaponAttackEvent(attacker, target, w, adjustedResult));
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

	@Override
	public String getName() {
		return "Precision Shot";
	}

	@Override
	public String getDescription() {
		return "Fires a shot that has +1 extra damage and +30% critical chance.";
	}

}
