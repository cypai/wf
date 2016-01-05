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
import com.pipai.wf.battle.action.verification.PredicateVerifier;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.damage.SpellDamageFunction;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.SpellWeapon;
import com.pipai.wf.item.weapon.Weapon;
import com.pipai.wf.spell.Spell;

public class TargetedSpellWeaponAction extends OverwatchableTargetedAction implements DefaultApRequiredComponent, DefaultWeaponAccuracyMixin {

	private static final Logger LOGGER = LoggerFactory.getLogger(TargetedSpellWeaponAction.class);

	private WeaponComponent weaponComponent = new WeaponComponentImpl();

	public TargetedSpellWeaponAction(BattleController controller, Agent performerAgent, Agent targetAgent, SpellWeapon weapon) {
		super(controller, performerAgent, targetAgent);
		setWeapon(weapon);
	}

	public TargetedSpellWeaponAction(BattleController controller, Agent performerAgent) {
		super(controller, performerAgent);
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
				new PredicateVerifier<Weapon>(weapon -> weapon instanceof SpellWeapon, getWeapon(), "Not a spell weapon"),
				new PredicateVerifier<Weapon>(weapon -> ((SpellWeapon) weapon).getSpell() != null, getWeapon(), "No readied spell"),
				new PredicateVerifier<Weapon>(weapon -> ((SpellWeapon) weapon).getSpell().canTargetAgent(), getWeapon(), "Spell is not targetable"));
	}

	@Override
	protected void performImpl(int owPenalty) throws IllegalActionException {
		Agent target = getTarget();
		SpellWeapon w = (SpellWeapon) getWeapon();
		Spell readiedSpell = w.getSpell();
		LOGGER.debug("Performed by '" + getPerformer().getName() + "' on '" + getTarget()
				+ "' with spell " + readiedSpell + " and owPenalty " + owPenalty);
		DamageResult result = getDamageCalculator().rollDamageGeneral(this, new SpellDamageFunction(readiedSpell), owPenalty);
		getPerformer().setAP(0);
		w.cast();
		getDamageDealer().doDamage(result, target);
		logBattleEvent(BattleEvent.castTargetEvent(getPerformer(), target, readiedSpell, result));
	}

	@Override
	public String getName() {
		Weapon w = getWeapon();
		if (w == null || !(w instanceof SpellWeapon) || ((SpellWeapon) w).getSpell() == null) {
			return "No Spell";
		}
		return ((SpellWeapon) w).getSpell().getName();
	}

	@Override
	public String getDescription() {
		Weapon w = getWeapon();
		if (w == null || !(w instanceof SpellWeapon) || ((SpellWeapon) w).getSpell() == null) {
			return "No Spell";
		}
		return ((SpellWeapon) w).getSpell().getDescription();
	}

}
