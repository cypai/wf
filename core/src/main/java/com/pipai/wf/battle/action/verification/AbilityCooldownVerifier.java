package com.pipai.wf.battle.action.verification;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.ActionVerificationResult;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.unit.ability.Ability;
import com.pipai.wf.unit.ability.component.CooldownComponent;

public class AbilityCooldownVerifier implements ActionVerifier {

	private Agent agent;
	private Class<? extends Ability> abilityClass;

	public AbilityCooldownVerifier(Agent agent, Class<? extends Ability> ability) {
		this.agent = agent;
		abilityClass = ability;
	}

	@Override
	public ActionVerificationResult verify(Action action) {
		if (agent == null) {
			return ActionVerificationResult.invalidResult("No unit specified");
		}
		if (abilityClass == null) {
			return ActionVerificationResult.invalidResult("No ability specified");
		}
		if (!agent.getAbilities().hasAbility(abilityClass)) {
			return ActionVerificationResult.invalidResult(agent.getName() + " does not have the required ability " + abilityClass.getName());
		}
		Ability ability = agent.getAbility(abilityClass);
		if (ability instanceof CooldownComponent && ((CooldownComponent) ability).onCooldown()) {
			return ActionVerificationResult.invalidResult("Ability is on cooldown");
		}
		return ActionVerificationResult.validResult();
	}
}
