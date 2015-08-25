package com.pipai.wf.battle.agent;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent.State;
import com.pipai.wf.battle.map.GridPosition;

public class AgentStateFactory {
	
	public static AgentState statsOnlyState(int hp, int mp, int ap, int mobility, int aim, int defense) {
		AgentState a = new AgentState();
		a.maxHP = hp;
		a.hp = hp;
		a.maxMP = mp;
		a.mp = mp;
		a.maxAP = ap;
		a.ap = ap;
		a.mobility = mobility;
		a.aim = aim;
		a.defense = defense;
		return a;
	}
	
	public static AgentState newBattleAgentState(Team team, GridPosition position, int hp, int mp, int ap, int mobility, int aim, int defense) {
		AgentState a = AgentStateFactory.statsOnlyState(hp, mp, ap, mobility, aim, defense);
		a.team = team;
		a.state = State.NEUTRAL;
		a.position = position;
		return a;
	}
	
	public static AgentState battleAgentFromStats(Team team, GridPosition position, AgentState stats) {
		AgentState a = AgentStateFactory.statsOnlyState(stats.maxHP, stats.maxMP, stats.maxAP, stats.mobility, stats.aim, stats.defense);
		a.team = team;
		a.state = State.NEUTRAL;
		a.position = position;
		return a;
	}
}
