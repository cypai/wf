package com.pipai.wf.battle.action.component;

import com.pipai.wf.battle.agent.Agent;

public interface HasTargetComponent {

	TargetComponent getTargetComponent();

	default Agent getTarget() {
		return getTargetComponent().getTarget();
	}

	default void setTarget(Agent target) {
		getTargetComponent().setTarget(target);
	}

}
