package com.pipai.wf.battle.action.component;

import com.pipai.wf.battle.agent.Agent;

public interface HasPerformerComponent extends PerformerComponent {

	PerformerComponent getPerformerComponent();

	@Override
	default Agent getPerformer() {
		return getPerformerComponent().getPerformer();
	}

	@Override
	default void setPerformer(Agent performer) {
		getPerformerComponent().setPerformer(performer);
	}

}
