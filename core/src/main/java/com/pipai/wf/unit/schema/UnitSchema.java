package com.pipai.wf.unit.schema;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.armor.Armor;
import com.pipai.wf.battle.armor.NoArmor;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.misc.BasicStats;
import com.pipai.wf.misc.HasBasicStats;
import com.pipai.wf.misc.HasName;
import com.pipai.wf.unit.ability.AbilityList;

public class UnitSchema implements HasName, HasBasicStats {

	private String name;
	private BasicStats basicStats;

	private AbilityList abilities;
	private Armor armor;
	private ArrayList<Weapon> weapons;

	private int expGiven;
	private int exp;

	/**
	 * Creates a schema using the AgentState's stats
	 */
	public UnitSchema(AgentState as) {
		name = as.getName();
		basicStats = as.getBasicStats();
		abilities = as.getAbilities().clone();
		armor = as.getArmor();
		weapons = as.getWeapons();
	}

	public UnitSchema(UnitSchema schema) {
		name = schema.getName();
		basicStats = schema.getBasicStats();
		abilities = schema.getAbilities();
		armor = schema.getArmor();
		weapons = schema.weapons;
	}

	public UnitSchema(String name, BasicStats stats) {
		this.name = name;
		basicStats = stats;
		abilities = new AbilityList();
		armor = new NoArmor();
		weapons = new ArrayList<>();
	}

	@JsonCreator
	public UnitSchema(
			@JsonProperty("name") String name,
			@JsonProperty("basicStats") BasicStats stats,
			@JsonProperty("abilities") AbilityList abilities,
			@JsonProperty("armor") Armor armor,
			@JsonProperty("weapons") List<Weapon> weapons,
			@JsonProperty("exp") int exp,
			@JsonProperty("expGiven") int expGiven) {
		this.name = name;
		basicStats = stats;
		this.abilities = abilities;
		this.armor = armor;
		this.weapons = new ArrayList<Weapon>(weapons);
		this.exp = exp;
		this.expGiven = expGiven;
	}

	@Override
	public String getName() {
		return name;
	}

	public AbilityList getAbilities() {
		return abilities.clone();
	}

	public Armor getArmor() {
		return armor;
	}

	public ArrayList<Weapon> getWeapons() {
		return weapons;
	}

	public int getExpGiven() {
		return expGiven;
	}

	public int getExp() {
		return exp;
	}

	@Override
	public BasicStats getBasicStats() {
		return basicStats;
	}

	@Override
	@JsonIgnore
	public int getHP() {
		return getBasicStats().getHP();
	}

	@Override
	@JsonIgnore
	public int getMaxHP() {
		return getBasicStats().getMaxHP();
	}

	@Override
	@JsonIgnore
	public int getMP() {
		return getBasicStats().getMP();
	}

	@Override
	@JsonIgnore
	public int getMaxMP() {
		return getBasicStats().getMaxMP();
	}

	@Override
	@JsonIgnore
	public int getAP() {
		return getBasicStats().getAP();
	}

	@Override
	@JsonIgnore
	public int getMaxAP() {
		return getBasicStats().getMaxAP();
	}

	@Override
	@JsonIgnore
	public int getAim() {
		return getBasicStats().getAim();
	}

	@Override
	@JsonIgnore
	public int getMobility() {
		return getBasicStats().getMobility();
	}

	@Override
	@JsonIgnore
	public int getDefense() {
		return getBasicStats().getDefense();
	}

}
