package com.pipai.wf.battle.action.verification;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.ActionVerificationResult;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.item.Item;

public class HasItemVerifier implements ActionVerifier {

	private Agent agent;
	private Item item;

	public HasItemVerifier(Agent agent, Item item) {
		this.agent = agent;
		this.item = item;
	}

	@Override
	public ActionVerificationResult verify(Action action) {
		if (agent == null) {
			return ActionVerificationResult.invalidResult("No unit specified");
		}
		if (item == null) {
			return ActionVerificationResult.invalidResult("No item specified");
		}
		if (!agent.getInventory().contains(item)) {
			return ActionVerificationResult.invalidResult(agent.getName() + " does not have " + item.getName());
		}
		return ActionVerificationResult.validResult();
	}

}
