package com.pipai.wf.battle.ai;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.exception.IllegalActionException;

public class OverwatchAI extends AI {

	private static final Logger logger = LoggerFactory.getLogger(OverwatchAI.class);

	protected LinkedList<Agent> enemyAgents, toAct;

	public OverwatchAI(BattleController battleController) {
		super(battleController);
		enemyAgents = new LinkedList<Agent>();
		for (Agent a : getBattleMap().getAgents()) {
			if (a.getTeam() == Team.ENEMY) {
				enemyAgents.add(a);
			}
		}
	}

	@Override
	protected void startTurnInit() {
		toAct = new LinkedList<Agent>();
		for (Agent a : enemyAgents) {
			if (!a.isKO()) {
				toAct.add(a);
			}
		}
	}

	@Override
	public void performMove() {
		if (toAct.isEmpty()) {
			endTurn();
			return;
		}
		Agent a = toAct.poll();
		if (a.getAP() > 0) {
			OverwatchAction ow = new OverwatchAction(a);
			try {
				getBattleController().performAction(ow);
			} catch (IllegalActionException e) {
				logger.error("AI tried to perform illegal move: " + e.getMessage());
			}
		}
	}

}
