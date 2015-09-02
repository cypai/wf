package com.pipai.wf.battle.ai;

import com.pipai.wf.battle.agent.Agent;

public class GeneralModularAI extends ModularAI {

	public GeneralModularAI(Agent a) {
		super(a);
	}

	@Override
	public ActionScore getBestMove() {
		return null;
	}

}
