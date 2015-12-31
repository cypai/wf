package com.pipai.wf.battle.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.damage.PercentageModifierList;
import com.pipai.wf.battle.damage.WeaponDamageFunction;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.weapon.Bow;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.battle.weapon.WeaponFlag;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.unit.ability.ArrowRainAbility;
import com.pipai.wf.util.UtilFunctions;

public class RangedWeaponAttackAction extends OverwatchableTargetedAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(RangedWeaponAttackAction.class);

	public RangedWeaponAttackAction(BattleController controller, Agent performerAgent, Agent targetAgent) {
		super(controller, performerAgent, targetAgent);
	}

	public RangedWeaponAttackAction(BattleController controller, Agent performerAgent) {
		super(controller, performerAgent);
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
	protected void performImpl(int owPenalty) throws IllegalActionException {
		Agent target = getTarget();
		LOGGER.debug("Performed by '" + getPerformer().getName() + "' on '" + getTarget() + "' with owPenalty " + owPenalty);
		if (target == null) {
			throw new IllegalActionException("Target not specified");
		}
		Agent performer = getPerformer();
		Weapon w = performer.getCurrentWeapon();
		if (w.hasFlag(WeaponFlag.NEEDS_AMMUNITION) && w.currentAmmo() == 0) {
			throw new IllegalActionException("Not enough ammo to fire " + w.getName());
		}
		DamageResult result = getDamageCalculator().rollDamageGeneral(this, new WeaponDamageFunction(w), owPenalty);
		if (performer.getCurrentWeapon() instanceof Bow
				&& performer.getAbilities().hasAbility(ArrowRainAbility.class)
				&& performer.getAP() == performer.getMaxAP()) {
			performer.setAP(performer.getAP() - 1);
		} else {
			performer.setAP(0);
		}
		getDamageDealer().doDamage(result, target);
		logBattleEvent(BattleEvent.rangedWeaponAttackEvent(performer, target, w, result));
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

	@Override
	public String getName() {
		return "Attack";
	}

	@Override
	public String getDescription() {
		Weapon weapon = getPerformer().getCurrentWeapon();
		if (weapon.hasFlag(WeaponFlag.NEEDS_AMMUNITION) && weapon.currentAmmo() <= 0) {
			return "Not enough ammunition";
		} else {
			return "Attack with the selected ranged weapon";
		}
	}

}
