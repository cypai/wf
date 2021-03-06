package com.pipai.wf.battle.damage;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.AgentCoverCalculator;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.item.weapon.Weapon;

public class TargetedActionCalculator {

	private BattleConfiguration config;

	public TargetedActionCalculator(BattleConfiguration config) {
		this.config = config;
	}

	public PercentageModifierList baseHitCalculation(BattleMap map, Agent performer, Agent target, Weapon weapon) {
		DefenseCalculator defenseCalc = new DefenseCalculator(map);
		PercentageModifierList p = new PercentageModifierList();
		p.add(new PercentageModifier("Aim", performer.getAim()));
		p.add(new PercentageModifier("Weapon Aim", weapon.flatAimModifier()));
		p.add(new PercentageModifier("Range", weapon.rangeAimModifier(performer.getDistanceFrom(target), config)));
		p.concat(performer.getStatusEffects().aimModifierList());
		p.add(new PercentageModifier("Defense", -defenseCalc.getDefenseAgainst(performer, target)));
		return p;
	}

	public PercentageModifierList baseCritCalculation(BattleMap map, Agent performer, Agent target, Weapon weapon) {
		AgentCoverCalculator coverCalc = new AgentCoverCalculator(map, config);
		PercentageModifierList p = new PercentageModifierList();
		p.add(new PercentageModifier("Weapon Base", weapon.flatCritProbabilityModifier()));
		p.add(new PercentageModifier("Range", weapon.rangeCritModifier(performer.getDistanceFrom(target), config)));
		if (coverCalc.isFlankedBy(target, performer)) {
			p.add(new PercentageModifier("No Cover", 30));
		}
		return p;
	}

}
