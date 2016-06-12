package com.pipai.wf.battle.action;

import java.util.Arrays;
import java.util.List;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.component.DefaultApRequiredComponent;
import com.pipai.wf.battle.action.component.DefaultWeaponAccuracyMixin;
import com.pipai.wf.battle.action.component.WeaponComponent;
import com.pipai.wf.battle.action.component.WeaponComponentImpl;
import com.pipai.wf.battle.action.verification.AbilityCooldownVerifier;
import com.pipai.wf.battle.action.verification.ActionVerifier;
import com.pipai.wf.battle.action.verification.BaseVerifier;
import com.pipai.wf.battle.action.verification.HasItemVerifier;
import com.pipai.wf.battle.action.verification.WeaponAmmoVerifier;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.damage.PercentageModifier;
import com.pipai.wf.battle.damage.PercentageModifierList;
import com.pipai.wf.battle.damage.WeaponDamageFunction;
import com.pipai.wf.battle.event.BattleEvent;
import com.pipai.wf.battle.event.RangedWeaponAttackEvent;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Weapon;
import com.pipai.wf.item.weapon.WeaponFlag;
import com.pipai.wf.unit.ability.PrecisionShotAbility;

public class PrecisionShotAction extends TargetedAction
		implements DefaultApRequiredComponent, DefaultWeaponAccuracyMixin {

	private WeaponComponent weaponComponent = new WeaponComponentImpl();

	public PrecisionShotAction(BattleController controller, Agent performerAgent, Weapon weapon) {
		super(controller, performerAgent);
		setWeapon(weapon);
	}

	public PrecisionShotAction(BattleController controller, Agent performerAgent, Agent targetAgent) {
		super(controller, performerAgent, targetAgent);
	}

	public PrecisionShotAction(BattleController controller, Agent performerAgent, Agent targetAgent, Weapon weapon) {
		super(controller, performerAgent, targetAgent);
		setWeapon(weapon);
	}

	@Override
	public WeaponComponent getWeaponComponent() {
		return weaponComponent;
	}

	@Override
	protected List<ActionVerifier> getVerifiers() {
		return Arrays.asList(
				BaseVerifier.getInstance(),
				new AbilityCooldownVerifier(getPerformer(), PrecisionShotAbility.class),
				new HasItemVerifier(getPerformer(), getWeapon()),
				new WeaponAmmoVerifier(getWeapon(), 1));
	}

	@Override
	public PercentageModifierList getCritCalculation() {
		PercentageModifierList calc = getTargetedActionCalculator().baseCritCalculation(getBattleMap(), getPerformer(),
				getTarget(), getWeapon());
		calc.add(new PercentageModifier("Precision Shot", 30));
		return calc;
	}

	@Override
	protected BattleEvent performImpl() throws IllegalActionException {
		Agent attacker = getPerformer();
		PrecisionShotAbility ability = (PrecisionShotAbility) attacker.getAbility(PrecisionShotAbility.class);
		if (ability == null) {
			throw new IllegalActionException(attacker.getName() + "does not have Precision Shot ability");
		}
		if (ability.onCooldown()) {
			throw new IllegalActionException("Ability is still on cooldown");
		}
		Agent target = getTarget();
		Weapon w = getWeapon();
		if (!w.hasFlag(WeaponFlag.PRECISE_SHOT)) {
			throw new IllegalActionException(w.getName() + " cannot use Precise Shot");
		}
		if (w.hasFlag(WeaponFlag.NEEDS_AMMUNITION) && w.getCurrentAmmo() == 0) {
			throw new IllegalActionException("Not enough ammo to fire " + w.getName());
		}
		DamageResult result = getDamageCalculator().rollDamageGeneral(this, new WeaponDamageFunction(w), 0);
		DamageResult adjustedResult = new DamageResult(result.isHit(), result.isCrit(),
				result.getDamage() + (result.isHit() ? 1 : 0), result.getDamageReduction());
		getDamageDealer().doDamage(adjustedResult, target);
		attacker.setAP(0);
		ability.startCooldown();
		return new RangedWeaponAttackEvent(attacker, target, w, adjustedResult).withActionName(getName());
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
