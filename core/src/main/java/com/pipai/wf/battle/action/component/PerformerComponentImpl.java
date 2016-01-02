package com.pipai.wf.battle.action.component;

import com.pipai.wf.battle.agent.Agent;

public class PerformerComponentImpl implements PerformerComponent {

	private Agent performer;

	@Override
	public Agent getPerformer() {
		return performer;
	}

	@Override
	public void setPerformer(Agent performer) {
		this.performer = performer;
	}

}
