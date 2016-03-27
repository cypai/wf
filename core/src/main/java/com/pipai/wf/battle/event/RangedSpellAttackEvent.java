package com.pipai.wf.battle.event;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.spell.Spell;

public class RangedSpellAttackEvent extends PerformerTargetEvent {

	public final Spell spell;
	public final DamageResult damageResult;

	public RangedSpellAttackEvent(Agent performer, Agent target, Spell spell, DamageResult damageResult) {
		super(performer, target);
		this.spell = spell;
		this.damageResult = damageResult;
	}

	@Override
	public String toString() {
		return performer.getName() + " attacked " + target.getName() + " with " + spell.getName() + " with result: [ "
				+ damageResult + " ]";
	}

}
