package com.pipai.wf.battle.action;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.component.DefaultApRequiredComponent;
import com.pipai.wf.battle.action.component.DefaultWeaponAccuracyMixin;
import com.pipai.wf.battle.action.component.WeaponComponent;
import com.pipai.wf.battle.action.component.WeaponComponentImpl;
import com.pipai.wf.battle.action.verification.ActionVerifier;
import com.pipai.wf.battle.action.verification.BaseVerifier;
import com.pipai.wf.battle.action.verification.HasItemVerifier;
import com.pipai.wf.battle.action.verification.WeaponAmmoVerifier;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.damage.WeaponDamageFunction;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Weapon;
import com.pipai.wf.item.weapon.WeaponFlag;
import com.pipai.wf.unit.ability.ArrowRainAbility;

public class RangedWeaponAttackAction extends OverwatchableTargetedAction implements DefaultApRequiredComponent, DefaultWeaponAccuracyMixin {

	private static final Logger LOGGER = LoggerFactory.getLogger(RangedWeaponAttackAction.class);

	private WeaponComponent weaponComponent = new WeaponComponentImpl();

	public RangedWeaponAttackAction() {
		// Call super
	}

	public RangedWeaponAttackAction(BattleController controller, Agent performerAgent) {
		super(controller, performerAgent);
	}

	public RangedWeaponAttackAction(BattleController controller, Agent performerAgent, Agent targetAgent, Weapon weapon) {
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
				new HasItemVerifier(getPerformer(), getWeapon()),
				new WeaponAmmoVerifier(getWeapon(), 1));
	}

	@Override
	protected void performImpl(int owPenalty) throws IllegalActionException {
		Agent performer = getPerformer();
		Weapon weapon = getWeapon();
		Agent target = getTarget();
		LOGGER.debug("Performed by '" + getPerformer().getName() + "' on '" + getTarget() + "' with owPenalty " + owPenalty);
		DamageResult result = getDamageCalculator().rollDamageGeneral(this, new WeaponDamageFunction(weapon), owPenalty);
		if (weapon.hasFlag(WeaponFlag.BOW)
				&& performer.getAbilities().hasAbility(ArrowRainAbility.class)
				&& performer.getAP() == performer.getMaxAP()) {
			performer.setAP(performer.getAP() - 1);
		} else {
			performer.setAP(0);
		}
		getDamageDealer().doDamage(result, target);
		logBattleEvent(BattleEvent.rangedWeaponAttackEvent(performer, target, weapon, result));
	}

	@Override
	public String getName() {
		return "Attack";
	}

	@Override
	public String getDescription() {
		return "Attack with the selected ranged weapon";
	}

}
