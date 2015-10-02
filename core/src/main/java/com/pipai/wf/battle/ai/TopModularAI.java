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
		this.enemyAgents = new ArrayList<Agent>();
		this.playerAgents = new ArrayList<Agent>();
		for (Agent a : getBattleMap().getAgents()) {
			if (a.getTeam() == Team.ENEMY) {
				this.enemyAgents.add(a);
			} else if (a.getTeam() == Team.PLAYER) {
				this.playerAgents.add(a);
			}
		}
	}

	private void resetAIModules() {
		this.ais = new ArrayList<ModularAI>();
		for (Agent a : this.enemyAgents) {
			if (!a.isKO() && a.getAP() > 0) {
				ais.add(new GeneralModularAI(getBattleMap(), a));
			}
		}
	}

	@Override
	public void performMove() {
		resetAIModules();
		if (this.ais.size() == 0) {
			endTurn();
			return;
		}
		ActionScore best = null;
		for (ModularAI ai : ais) {
			ActionScore as = ai.getBestMove();
			best = as.compareAndReturnBetter(best);
		}
		try {
			getBattleController().performAction(best.action);
		} catch (IllegalActionException e) {
			logger.error("AI tried to perform illegal move: " + e.getMessage());
		}
	}

}
