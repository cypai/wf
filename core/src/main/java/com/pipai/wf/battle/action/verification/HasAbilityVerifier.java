package com.pipai.wf.battle.action.verification;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.ActionVerificationResult;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.unit.ability.Ability;

public class HasAbilityVerifier implements ActionVerifier {

	private Agent agent;
	private Class<? extends Ability> ability;

	public HasAbilityVerifier(Agent agent, Class<? extends Ability> ability) {
		this.agent = agent;
		this.ability = ability;
	}

	@Override
	public ActionVerificationResult verify(Action action) {
		if (agent == null) {
			return ActionVerificationResult.invalidResult("No unit specified");
		}
		if (ability == null) {
			return ActionVerificationResult.invalidResult("No ability specified");
		}
		if (!agent.getAbilities().hasAbility(ability)) {
			return ActionVerificationResult
					.invalidResult(agent.getName() + " does not have the required ability " + ability.getName());
		}
		return ActionVerificationResult.validResult();
	}

}
