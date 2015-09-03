package com.pipai.wf.battle.ai;

import java.util.ArrayList;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.BattleMap;

public abstract class ModularAI {

	private BattleMap map;
	private Agent aiAgent;

	public ModularAI(BattleMap map, Agent a) {
		this.map = map;
		aiAgent = a;
	}

	public abstract ActionScore getBestMove();

	public Agent getAgent() { return aiAgent; }

	public BattleMap getBattleMap() { return map; }

	public ArrayList<Agent> getAgentsInTeam(Team t) {
		ArrayList<Agent> l = new ArrayList<Agent>();
		for (Agent a : map.getAgents()) {
			if (a.getTeam() == t) {
				l.add(a);
			}
		}
		return l;
	}

}
