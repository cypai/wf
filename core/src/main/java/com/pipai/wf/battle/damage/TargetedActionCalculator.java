package com.pipai.wf.battle.damage;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.weapon.Weapon;

public class TargetedActionCalculator {

	public static PercentageModifierList baseHitCalculation(Agent performer, Agent target) {
		Weapon weapon = performer.getCurrentWeapon();
		PercentageModifierList p = new PercentageModifierList();
		p.add(new PercentageModifier("Aim", performer.getBaseAim()));
		p.add(new PercentageModifier("Weapon Aim", weapon.flatAimModifier()));
		p.add(new PercentageModifier("Range", weapon.rangeAimModifier(performer.getDistanceFrom(target), null)));
		p.concat(performer.getStatusEffects().aimModifierList());
		p.add(new PercentageModifier("Defense", -target.getDefense(performer)));
		return p;
	}

	public static PercentageModifierList baseCritCalculation(Agent performer, Agent target) {
		Weapon weapon = performer.getCurrentWeapon();
		PercentageModifierList p = new PercentageModifierList();
		p.add(new PercentageModifier("Weapon Base", weapon.flatCritProbabilityModifier()));
		p.add(new PercentageModifier("Range", weapon.rangeCritModifier(performer.getDistanceFrom(target), null)));
		if (target.isFlankedBy(performer)) {
			p.add(new PercentageModifier("No Cover", 30));
		}
		return p;
	}

}
