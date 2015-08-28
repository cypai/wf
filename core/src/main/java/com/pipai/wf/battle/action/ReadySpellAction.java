package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.exception.IllegalActionException;

public class ReadySpellAction extends AlterStateAction {
	
	private Spell spell;
	
	public ReadySpellAction(Agent performerAgent, Spell spell) {
		super(performerAgent);
		this.spell = spell;
	}
	
	public int getAPRequired() { return 1; }

	@Override
	protected void performImpl() throws IllegalActionException {
		getPerformer().readySpell(spell);
	}
	
}
