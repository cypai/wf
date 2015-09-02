package com.pipai.wf.battle.ai;

import com.pipai.wf.battle.agent.Agent;

public abstract class ModularAI {

	private Agent aiAgent;

	public ModularAI(Agent a) {
		aiAgent = a;
	}

	public abstract ActionScore getBestMove();

	public Agent getAgent() { return aiAgent; }

}
