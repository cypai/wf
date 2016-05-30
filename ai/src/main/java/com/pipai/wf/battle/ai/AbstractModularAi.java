package com.pipai.wf.battle.ai;

import java.util.ArrayList;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.ai.utils.ActionScore;
import com.pipai.wf.battle.map.BattleMap;

public abstract class AbstractModularAi {

	private BattleController battleController;
	private BattleMap battleMap;
	private Agent aiAgent;

	public AbstractModularAi(BattleController controller, Agent a) {
		this.battleController = controller;
		battleMap = controller.getBattleMap();
		aiAgent = a;
	}

	public abstract ActionScore getBestMove();

	public Agent getAiAgent() {
		return aiAgent;
	}

	public BattleMap getBattleMap() {
		return battleMap;
	}

	public BattleController getBattleController() {
		return battleController;
	}

	public BattleConfiguration getBattleConfiguration() {
		return battleController.getBattleConfiguration();
	}

	public ArrayList<Agent> getAgentsInTeam(Team t) {
		ArrayList<Agent> l = new ArrayList<>();
		for (Agent a : battleMap.getAgents()) {
			if (a.getTeam() == t) {
				l.add(a);
			}
		}
		return l;
	}

}
