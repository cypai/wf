package com.pipai.wf.battle.agent;

import com.pipai.wf.battle.agent.Agent.State;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.map.GridPosition;

public class AgentState {
	
	public Team team;
	public int hp, maxHP;
	public int mp, maxMP;
	public int ap, maxAP;
	public int mobility, aim, defense;
	public GridPosition position;
	public State state;
	
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
		AgentState a = AgentState.statsOnlyState(hp, mp, ap, mobility, aim, defense);
		a.team = team;
		a.state = State.NEUTRAL;
		a.position = position;
		return a;
	}
	
	public static AgentState battleAgentFromStats(Team team, GridPosition position, AgentState stats) {
		AgentState a = AgentState.statsOnlyState(stats.maxHP, stats.maxMP, stats.maxAP, stats.mobility, stats.aim, stats.defense);
		a.team = team;
		a.state = State.NEUTRAL;
		a.position = position;
		return a;
	}
	
	private AgentState() {
		
	}
	
	/*
	 * MapString version
	 */
	public String generateString() {
		String s = "a ";
		s += String.valueOf(position.x) + " " + String.valueOf(position.y) + " ";
		s += team + " ";
		s += String.valueOf(hp) + " " + String.valueOf(maxHP) + " ";
		s += String.valueOf(ap) + " " + String.valueOf(maxAP) + " ";
		s += String.valueOf(mobility) + " ";
		s += state;
		return s;
	}
	
	/*
	 * Readable version
	 */
	public String toString() {
		String s = "";
		s += "Position: " + position + "\n";
		s += "Team: " + team + "\n";
		s += "HP: " + String.valueOf(hp) + "/" + String.valueOf(maxHP) + "\n";
		s += "AP: " + String.valueOf(ap) + "/" + String.valueOf(maxAP) + "\n";
		s += "Mobility: " + String.valueOf(mobility) + "\n";
		s += "State: " + state + "\n";
		return s;
	}
	
	public AgentState statsOnlyCopy() {
		return statsOnlyState(maxHP, maxMP, maxAP, mobility, aim, defense);
	}
	
}