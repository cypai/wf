package com.pipai.wf.battle.ai;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.ai.modules.GenericAiModule;
import com.pipai.wf.battle.ai.utils.ActionScore;
import com.pipai.wf.exception.IllegalActionException;

public class ModularAi extends Ai {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModularAi.class);

	private ArrayList<Agent> enemyAgents, playerAgents;
	private ArrayList<AbstractModularAi> ais;

	public ModularAi(BattleController battleController) {
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
		ais = new ArrayList<AbstractModularAi>();
		for (Agent a : enemyAgents) {
			if (!a.isKO() && a.getAP() > 0) {
				ais.add(new GenericAiModule(getBattleController(), a));
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
		for (AbstractModularAi ai : ais) {
			ActionScore as = ai.getBestMove();
			best = as.compareAndReturnBetter(best);
		}
		try {
			// may be null if ais is empty
			if (best != null) {
				LOGGER.debug("AI Selection: {}", best);
				best.getAction().perform();
			}
		} catch (IllegalActionException e) {
			LOGGER.error("AI tried to perform illegal move: " + e.getMessage());
		}
	}

}
