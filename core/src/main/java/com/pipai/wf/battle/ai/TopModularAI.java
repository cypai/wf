package com.pipai.wf.battle.ai;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.exception.IllegalActionException;

public class TopModularAI extends AI {

	private static final Logger logger = LoggerFactory.getLogger(TopModularAI.class);

	private ArrayList<Agent> enemyAgents, playerAgents;
	private ArrayList<ModularAI> ais;

	public TopModularAI(BattleController battleController) {
		super(battleController);
		enemyAgents = new ArrayList<Agent>();
		playerAgents = new ArrayList<Agent>();
		for (Agent a : getBattleMap().getAgents()) {
			if (a.getTeam() == Team.ENEMY) {
				enemyAgents.add(a);
			} else if (a.getTeam() == Team.PLAYER) {
				playerAgents.add(a);
			}
		}
	}

	private void resetAIModules() {
		ais = new ArrayList<ModularAI>();
		for (Agent a : enemyAgents) {
			if (!a.isKO() && a.getAP() > 0) {
				ais.add(new GeneralModularAI(getBattleController(), a));
			}
		}
	}

	@Override
	public void performMove() {
		resetAIModules();
		if (ais.size() == 0) {
			endTurn();
			return;
		}
		ActionScore best = null;
		for (ModularAI ai : ais) {
			ActionScore as = ai.getBestMove();
			best = as.compareAndReturnBetter(best);
		}
		try {
			best.action.perform();
		} catch (IllegalActionException e) {
			logger.error("AI tried to perform illegal move: " + e.getMessage());
		}
	}

}
