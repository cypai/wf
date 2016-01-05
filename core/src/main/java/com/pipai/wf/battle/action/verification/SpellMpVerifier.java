package com.pipai.wf.battle.action.verification;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.ActionVerificationResult;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.spell.Spell;

public class SpellMpVerifier implements ActionVerifier {

	private Agent agent;
	private Spell spell;

	public SpellMpVerifier(Agent agent, Spell spell) {
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
		if (agent.getMP() < spell.requiredMP()) {
			return ActionVerificationResult.invalidResult("Not enough MP");
		}
		return ActionVerificationResult.validResult();
	}

}
