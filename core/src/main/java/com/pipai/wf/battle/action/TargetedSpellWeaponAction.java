package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.DamageCalculator;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.damage.PercentageModifierList;
import com.pipai.wf.battle.damage.SpellDamageFunction;
import com.pipai.wf.battle.damage.TargetedActionCalculator;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.battle.weapon.SpellWeapon;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.util.UtilFunctions;

public class TargetedSpellWeaponAction extends TargetedWithAccuracyActionOWCapable {

	public final Spell spell;

	public TargetedSpellWeaponAction(Agent performerAgent, Agent targetAgent) {
		super(performerAgent, targetAgent);
		this.spell = getWeapon().getSpell();
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
		return TargetedActionCalculator.baseHitCalculation(a, target);
	}

	@Override
	public PercentageModifierList getCritCalculation() {
		Agent a = getPerformer();
		Agent target = getTarget();
		return TargetedActionCalculator.baseCritCalculation(a, target);
	}

	@Override
	public void performOnOverwatch(BattleEvent parent) throws IllegalActionException {
		Agent a = getPerformer();
		Agent target = getTarget();
		SpellWeapon w = getWeapon();
		Spell readiedSpell = w.getSpell();
		if (readiedSpell == null) {
			throw new IllegalActionException("No readied spell available");
		}
		if (readiedSpell != spell) {
			throw new IllegalActionException("Spell being casted is not the same as the readied spell");
		}
		if (!readiedSpell.canTargetAgent()) {
			throw new IllegalActionException("Cannot target with " + readiedSpell.name());
		}
		DamageResult result = DamageCalculator.rollDamageGeneral(this, new SpellDamageFunction(spell), 1);
		a.setAP(0);
		w.cast();
		target.takeDamage(result.damage);
		parent.addChainEvent(BattleEvent.overwatchActivationEvent(a, target, this, target.getPosition(), result));
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		Agent a = getPerformer();
		Agent target = getTarget();
		SpellWeapon w = getWeapon();
		Spell readiedSpell = w.getSpell();
		if (readiedSpell == null) {
			throw new IllegalActionException("No readied spell available");
		}
		if (readiedSpell != spell) {
			throw new IllegalActionException("Spell being casted is not the same as the readied spell");
		}
		if (!readiedSpell.canTargetAgent()) {
			throw new IllegalActionException("Cannot target with " + readiedSpell.name());
		}
		DamageResult result = DamageCalculator.rollDamageGeneral(this, new SpellDamageFunction(spell), 0);
		a.setAP(0);
		w.cast();
		target.takeDamage(result.damage);
		log(BattleEvent.castTargetEvent(a, target, spell, result));
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

	@Override
	public String name() {
		if (spell == null) {
			return "No Spell";
		}
		return spell.name();
	}

	@Override
	public String description() {
		return spell.description();
	}

}
