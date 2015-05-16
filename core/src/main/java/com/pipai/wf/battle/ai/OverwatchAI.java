package com.pipai.wf.battle.ai;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.attack.SimpleRangedAttack;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.exception.IllegalActionException;

public class OverwatchAI extends AI {
	
	public OverwatchAI(BattleController battleController) {
		super(battleController);
	}

	@Override
	public void notifyBattleEvent(BattleEvent ev) {
		
	}
	
	@Override
	public void performMove() {
		if (this.toAct.isEmpty()) {
			return;
		}
		Agent a = this.toAct.poll();
		if (a.getAP() > 0) {
			OverwatchAction ow = new OverwatchAction(a, new SimpleRangedAttack());
			try {
				this.battleController.performAction(ow);
			} catch (IllegalActionException e) {
				System.out.println("AI tried to perform illegal move: " + e.getMessage());
			}
		}
		if (this.toAct.isEmpty()) {
			this.battleController.endTurn();
		}
	}
	
}
