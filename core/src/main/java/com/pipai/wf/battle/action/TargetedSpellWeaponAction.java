package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.DamageCalculator;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.damage.SpellDamageFunction;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.battle.weapon.SpellWeapon;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.exception.IllegalActionException;

public class TargetedSpellWeaponAction extends TargetedWithAccuracyAction {

	public final Spell spell;

	private SpellWeapon getWeapon() {
		Weapon w = getPerformer().getCurrentWeapon();
		if (!(w instanceof SpellWeapon)) {
			throw new IllegalArgumentException("Currently selected weapon is not a spell weapon");
		}
		return (SpellWeapon) w;
	}

	public TargetedSpellWeaponAction(Agent performerAgent, Agent targetAgent, Spell spell) {
		super(performerAgent, targetAgent);
		this.spell = spell;
	}

	@Override
	public int toHit() {
		Agent a = getPerformer();
		Agent target = getTarget();
		int base = spell.getAccuracy(a, target, a.getDistanceFrom(target));
		return base;
	}

	@Override
	public int toCrit() {
		Agent a = getPerformer();
		Agent target = getTarget();
		int base = spell.getCritPercentage(a, target, a.getDistanceFrom(target));
		return base;
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
		log(BattleEvent.castTargetEvent(a, target, spell, result));
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

	@Override
	public String name() {
		return spell.name();
	}

	@Override
	public String description() {
		return spell.description();
	}

}
