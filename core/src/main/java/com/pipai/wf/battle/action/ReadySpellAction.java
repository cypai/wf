package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.exception.IllegalActionException;

public class ReadySpellAction extends Action {
	
	private Spell spell;
	
	public ReadySpellAction(Agent performerAgent, Spell spell) {
		super(performerAgent);
		this.spell = spell;
	}
	
	public void perform() throws IllegalActionException {
		super.perform();
		this.performerAgent.readySpell(spell);
	}

	public int getAPRequired() { return 1; }
	
}
