package com.pipai.wf.battle.action.component;

import com.pipai.wf.battle.agent.Agent;

public class PerformerTargetComponentImpl implements TargetComponent, PerformerComponent {

	private Agent performer;
	private Agent target;

	@Override
	public Agent getPerformer() {
		return performer;
	}

	@Override
	public void setPerformer(Agent performer) {
		this.performer = performer;
	}

	@Override
	public Agent getTarget() {
		return target;
	}

	@Override
	public void setTarget(Agent target) {
		this.target = target;
	}

}
