package com.pipai.wf.battle.agent;

import java.util.ArrayList;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent.State;
import com.pipai.wf.battle.armor.Armor;
import com.pipai.wf.battle.armor.NoArmor;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.unit.ability.Ability;
import com.pipai.wf.unit.ability.AbilityList;

public class AgentState {

	public Team team;
	public int hp, maxHP;
	public int mp, maxMP;
	public int ap, maxAP;
	public int mobility, aim, defense;
	public GridPosition position;
	public State state;
	public AbilityList abilities;
	public ArrayList<Weapon> weapons;
	public Armor armor;
	public String name;
	private final BattleConfiguration config;

	public AgentState(BattleConfiguration config) {
		this.config = config;
		abilities = new AbilityList();
		weapons = new ArrayList<Weapon>();
		armor = new NoArmor();
	}

	public BattleConfiguration getBattleConfiguration() {
		return config;
	}

	public void addAbilities(AbilityList abilityList) {
		for (Ability a : abilityList) {
			abilities.add(a.clone());
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
	@Override
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
		AgentStateFactory factory = new AgentStateFactory(config);
		return factory.statsOnlyState(maxHP, maxMP, maxAP, mobility, aim, defense);
	}

}