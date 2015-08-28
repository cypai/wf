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

public class GeneralTargetedSpellAction extends TargetedWithAccuracyAction {
	
	Spell spell;

	public GeneralTargetedSpellAction(Agent performerAgent, Agent targetAgent, Spell spell) {
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
	protected void performImpl() throws IllegalActionException {
		Agent a = getPerformer();
		Agent target = getTarget();
		Weapon w = a.getCurrentWeapon();
		if (!(w instanceof SpellWeapon)) {
			throw new IllegalActionException("Currently selected weapon is not a spell weapon");
		}
		Spell readiedSpell = ((SpellWeapon)w).getSpell();
		if (readiedSpell == null) {
			throw new IllegalActionException("No readied spell available");
		}
		if (readiedSpell.equals(spell)) {
			throw new IllegalActionException("Spell being casted is not the same as the readied spell");
		}
		if (!readiedSpell.canTargetAgent()) {
			throw new IllegalActionException("Cannot target with " + readiedSpell.name());
		}
		DamageResult result = DamageCalculator.rollDamageGeneral(this, new SpellDamageFunction(spell));
		a.setAP(0);
		log(BattleEvent.castTargetEvent(a, target, spell, result));
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

}
