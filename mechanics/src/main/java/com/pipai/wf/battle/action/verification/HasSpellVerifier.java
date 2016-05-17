package com.pipai.wf.battle.action.verification;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.ActionVerificationResult;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.spell.Spell;

public class HasSpellVerifier implements ActionVerifier {

	private Agent agent;
	private Spell spell;

	public HasSpellVerifier(Agent agent, Spell spell) {
		this.agent = agent;
		this.spell = spell;
	}

	@Override
	public ActionVerificationResult verify(Action action) {
		if (agent == null) {
			return ActionVerificationResult.invalidResult("No unit specified");
		}
		if (spell == null) {
			return ActionVerificationResult.invalidResult("No spell specified");
		}
		if (!agent.getAbilities().hasSpell(spell)) {
			return ActionVerificationResult.invalidResult(agent.getName() + " does not have " + spell.getName());
		}
		return ActionVerificationResult.validResult();
	}

}
