package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.component.HasPerformerComponent;
import com.pipai.wf.battle.action.component.PerformerComponent;
import com.pipai.wf.battle.action.component.PerformerComponentImpl;
import com.pipai.wf.battle.agent.Agent;

public abstract class PerformerAction extends Action implements HasPerformerComponent {

	private PerformerComponent performerComponent = new PerformerComponentImpl();

	public PerformerAction(BattleController controller, Agent performerAgent) {
		super(controller);
		setPerformer(performerAgent);
	}

	public PerformerAction(BattleController controller) {
		super(controller);
	}

	public PerformerAction() {
		// Call super
	}

	@Override
	public PerformerComponent getPerformerComponent() {
		return performerComponent;
	}

}
