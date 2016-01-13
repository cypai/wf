package com.pipai.wf.battle.event;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.spell.Spell;

public class ReadySpellEvent extends BattleEvent {

	public final Agent performer;
	public final Spell spell;
	public final boolean quicken;

	public ReadySpellEvent(Agent performer, Spell spell, boolean quicken) {
		this.performer = performer;
		this.spell = spell;
		this.quicken = quicken;
	}

	@Override
	public String toString() {
		return performer.getName() + " performed " + (quicken ? "Quicken: " : "") + spell.getName();
	}
}
