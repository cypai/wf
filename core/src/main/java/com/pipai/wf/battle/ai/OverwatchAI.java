package com.pipai.wf.battle.ai;

import java.util.LinkedList;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.action.WeaponActionFactory;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.exception.IllegalActionException;

public class OverwatchAI extends AI {
	protected LinkedList<Agent> enemyAgents, toAct;

	public OverwatchAI(BattleController battleController) {
		super(battleController);
		this.enemyAgents = new LinkedList<Agent>();
		for (Agent a : this.map.getAgents()) {
			if (a.getTeam() == Team.ENEMY) {
				this.enemyAgents.add(a);
			}
		}
	}

	@Override
	public void startTurn() {
		super.startTurn();
		this.toAct = new LinkedList<Agent>();
		for (Agent a : this.enemyAgents) {
			if (!a.isKO()) {
				this.toAct.add(a);
			}
		}
	}

	@Override
	public void performMove() {
		if (this.toAct.isEmpty()) {
			this.endTurn();
			return;
		}
		Agent a = this.toAct.poll();
		if (a.getAP() > 0) {
			OverwatchAction ow = new OverwatchAction(a, WeaponActionFactory.defaultWeaponActionClass(a));
			try {
				this.battleController.performAction(ow);
			} catch (IllegalActionException e) {
				System.out.println("AI tried to perform illegal move: " + e.getMessage());
			}
		}
	}

}
