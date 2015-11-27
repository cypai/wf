package com.pipai.wf.battle.ai;

import java.util.ArrayList;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.BattleMap;

public abstract class ModularAI {

	private BattleController controller;
	private BattleMap map;
	private Agent aiAgent;

	public ModularAI(BattleController controller, Agent a) {
		this.controller = controller;
		map = controller.getBattleMap();
		aiAgent = a;
	}

	public abstract ActionScore getBestMove();

	public Agent getAgent() {
		return aiAgent;
	}

	public BattleMap getBattleMap() {
		return map;
	}

	public BattleController getBattleController() {
		return controller;
	}

	public BattleConfiguration getBattleConfiguration() {
		return controller.getBattleConfiguration();
	}

	public ArrayList<Agent> getAgentsInTeam(Team t) {
		ArrayList<Agent> l = new ArrayList<>();
		for (Agent a : map.getAgents()) {
			if (a.getTeam() == t) {
				l.add(a);
			}
		}
		return l;
	}

}
