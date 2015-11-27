package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;

public interface TargetedActionable {

	public TargetedAction getAction(BattleController controller, Agent performer, Agent target);

}
