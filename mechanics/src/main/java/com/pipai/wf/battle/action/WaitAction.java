package com.pipai.wf.battle.action;

import java.util.Arrays;
import java.util.List;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.component.DefaultApRequiredComponent;
import com.pipai.wf.battle.action.verification.ActionVerifier;
import com.pipai.wf.battle.action.verification.BaseVerifier;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.exception.IllegalActionException;

public class WaitAction extends PerformerAction implements DefaultApRequiredComponent {

	public WaitAction(BattleController controller, Agent performerAgent) {
		super(controller, performerAgent);
	}

	@Override
	protected List<ActionVerifier> getVerifiers() {
		return Arrays.asList(BaseVerifier.getInstance());
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		getPerformer().setAP(0);
	}

	@Override
	public String getName() {
		return "Wait";
	}

	@Override
	public String getDescription() {
		return "Do not do anything";
	}

}
