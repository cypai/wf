package com.pipai.wf.battle.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.component.ApRequiredComponent;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.damage.PercentageModifierList;
import com.pipai.wf.battle.damage.SpellDamageFunction;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.battle.weapon.SpellWeapon;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.exception.IllegalActionException;

public class TargetedSpellWeaponAction extends OverwatchableTargetedAction implements ApRequiredComponent {

	private static final Logger LOGGER = LoggerFactory.getLogger(TargetedSpellWeaponAction.class);

	private final Spell spell;

	public TargetedSpellWeaponAction(BattleController controller, Agent performerAgent, Agent targetAgent) {
		super(controller, performerAgent, targetAgent);
		spell = getWeapon().getSpell();
	}

	public TargetedSpellWeaponAction(BattleController controller, Agent performerAgent) {
		super(controller, performerAgent);
		spell = getWeapon().getSpell();
	}

	private SpellWeapon getWeapon() {
		Weapon w = getPerformer().getCurrentWeapon();
		if (w == null) {
			throw new IllegalArgumentException("No weapon is equipped");
		}
		if (!(w instanceof SpellWeapon)) {
			throw new IllegalArgumentException("Currently selected weapon is not a spell weapon");
		}
		return (SpellWeapon) w;
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
		SpellWeapon w = getWeapon();
		Spell readiedSpell = w.getSpell();
		LOGGER.debug("Performed by '" + getPerformer().getName() + "' on '" + getTarget()
				+ "' with spell " + readiedSpell + " and owPenalty " + owPenalty);
		if (target == null) {
			throw new IllegalActionException("Target not specified");
		}
		Agent a = getPerformer();
		if (readiedSpell == null) {
			throw new IllegalActionException("No readied spell available");
		}
		if (readiedSpell != spell) {
			throw new IllegalActionException("Spell being casted is not the same as the readied spell");
		}
		if (!readiedSpell.canTargetAgent()) {
			throw new IllegalActionException("Cannot target with " + readiedSpell.getName());
		}
		DamageResult result = getDamageCalculator().rollDamageGeneral(this, new SpellDamageFunction(spell), owPenalty);
		a.setAP(0);
		w.cast();
		getDamageDealer().doDamage(result, target);
		logBattleEvent(BattleEvent.castTargetEvent(a, target, spell, result));
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

	@Override
	public String getName() {
		if (spell == null) {
			return "No Spell";
		}
		return spell.getName();
	}

	@Override
	public String getDescription() {
		return spell.getDescription();
	}

}
