package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;

public abstract class AlterStateAction extends Action {

	public AlterStateAction(BattleController controller, Agent performerAgent) {
		super(controller, performerAgent);
	}

}
