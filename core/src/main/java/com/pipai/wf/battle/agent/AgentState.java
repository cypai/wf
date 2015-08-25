package com.pipai.wf.battle.agent;

import java.util.ArrayList;
import java.util.List;

import com.pipai.wf.battle.agent.Agent.State;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.unit.ability.Ability;
import com.pipai.wf.unit.ability.AbilityFactory;

public class AgentState {
	
	public Team team;
	public int hp, maxHP;
	public int mp, maxMP;
	public int ap, maxAP;
	public int mobility, aim, defense;
	public GridPosition position;
	public State state;
	public ArrayList<Ability> abilities;
	
	public AgentState() {
		abilities = new ArrayList<Ability>();
	}
	
	public void addAbilities(List<Ability> abilityList) {
		for (Ability a : abilityList) {
			abilities.add(AbilityFactory.clone(a));
		}
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
		return AgentStateFactory.statsOnlyState(maxHP, maxMP, maxAP, mobility, aim, defense);
	}
	
}