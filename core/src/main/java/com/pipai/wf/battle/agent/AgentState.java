package com.pipai.wf.battle.agent;

import java.util.ArrayList;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.armor.Armor;
import com.pipai.wf.battle.armor.NoArmor;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.misc.BasicStats;
import com.pipai.wf.misc.HasBasicStats;
import com.pipai.wf.misc.HasName;
import com.pipai.wf.unit.ability.Ability;
import com.pipai.wf.unit.ability.AbilityList;

public class AgentState implements HasBasicStats, HasName {

	private Team team;
	private BasicStats basicStats;
	private GridPosition position;
	private State state;
	private AbilityList abilities;
	private ArrayList<Weapon> weapons;
	private Armor armor;
	private String name;

	private int level;
	private int expGiven;
	private int exp;

	public AgentState(String name, BasicStats stats) {
		this.name = name;
		basicStats = stats;
		abilities = new AbilityList();
		weapons = new ArrayList<Weapon>();
		armor = new NoArmor();
	}

	public AgentState(Agent agent) {
		team = agent.getTeam();
		basicStats = agent.getBasicStats();
		position = agent.getPosition();
		state = agent.getState();
		abilities = agent.getAbilities();
		weapons = agent.getWeapons();
		armor = agent.getArmor();
		name = agent.getName();
		level = agent.getLevel();
		exp = agent.getExp();
		expGiven = agent.getExpGiven();
	}

	public void addAbilities(AbilityList abilityList) {
		for (Ability a : abilityList) {
			getAbilities().add(a.clone());
		}
	}

	/*
	 * MapString version
	 */
	public String generateString() {
		String s = "a ";
		s += String.valueOf(getPosition().getX()) + " " + String.valueOf(getPosition().getY()) + " ";
		s += getTeam() + " ";
		s += String.valueOf(getHP()) + " " + String.valueOf(getMaxHP()) + " ";
		s += String.valueOf(getAP()) + " " + String.valueOf(getMaxAP()) + " ";
		s += String.valueOf(getMobility()) + " ";
		s += state;
		return s;
	}

	/*
	 * Readable version
	 */
	@Override
	public String toString() {
		String s = "";
		s += "Position: " + getPosition() + "\n";
		s += "Team: " + getTeam() + "\n";
		s += "HP: " + String.valueOf(getHP()) + "/" + String.valueOf(getMaxHP()) + "\n";
		s += "AP: " + String.valueOf(getAP()) + "/" + String.valueOf(getMaxAP()) + "\n";
		s += "Mobility: " + String.valueOf(getMobility()) + "\n";
		s += "State: " + state + "\n";
		return s;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public BasicStats getBasicStats() {
		return basicStats;
	}

	public ArrayList<Weapon> getWeapons() {
		return weapons;
	}

	public void setWeapons(ArrayList<Weapon> weapons) {
		this.weapons = weapons;
	}

	public Armor getArmor() {
		return armor;
	}

	public void setArmor(Armor armor) {
		this.armor = armor;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public GridPosition getPosition() {
		return position;
	}

	public void setPosition(GridPosition position) {
		this.position = position;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public AbilityList getAbilities() {
		return abilities;
	}

	public void setAbilities(AbilityList abilities) {
		this.abilities = abilities;
	}

	public int getLevel() {
		return level;
	}

	public int getExp() {
		return exp;
	}

	public int getExpGiven() {
		return expGiven;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public void setExpGiven(int expGiven) {
		this.expGiven = expGiven;
	}

}
