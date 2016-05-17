package com.pipai.wf.unit.schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.inventory.AgentInventory;
import com.pipai.wf.misc.BasicStats;
import com.pipai.wf.unit.ability.AbilityList;

public class MutableUnitSchema implements UnitSchema {

	private String name;
	private BasicStats basicStats;

	private AbilityList abilities;
	private AgentInventory inventory;

	private int level;
	private int expGiven;
	private int exp;

	/**
	 * Creates a schema using the Agent's stats
	 */
	public MutableUnitSchema(Agent agent) {
		name = agent.getName();
		basicStats = new BasicStats(agent.getHP(), agent.getMaxHP(), agent.getMaxMP(), agent.getMaxMP(),
				agent.getMaxAP(), agent.getMaxAP(),
				agent.getAim(), agent.getMobility(), agent.getDefense());
		abilities = agent.getAbilities().deepCopyAsNew();
		inventory = agent.getInventory();
		level = agent.getLevel();
		exp = agent.getExp();
		expGiven = agent.getExpGiven();
	}

	public MutableUnitSchema(UnitSchema schema) {
		name = schema.getName();
		basicStats = schema.getBasicStats();
		abilities = schema.getAbilities();
		inventory = schema.getInventory();
		level = schema.getLevel();
		exp = schema.getExp();
		expGiven = schema.getExpGiven();
	}

	public MutableUnitSchema(String name, BasicStats stats) {
		this.name = name;
		basicStats = stats;
		abilities = new AbilityList();
		// TODO: Magic number here...
		inventory = new AgentInventory(3);
		level = 1;
	}

	@JsonCreator
	public MutableUnitSchema(
			@JsonProperty("name") String name,
			@JsonProperty("basicStats") BasicStats stats,
			@JsonProperty("abilities") AbilityList abilities,
			@JsonProperty("inventory") AgentInventory inventory,
			@JsonProperty("level") int level,
			@JsonProperty("exp") int exp,
			@JsonProperty("expGiven") int expGiven) {
		this.name = name;
		basicStats = stats;
		this.abilities = abilities;
		this.inventory = inventory;
		this.level = level;
		this.exp = exp;
		this.expGiven = expGiven;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public AgentInventory getInventory() {
		return inventory;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public int getExpGiven() {
		return expGiven;
	}

	@Override
	public int getExp() {
		return exp;
	}

	@Override
	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public void setExp(int exp) {
		this.exp = exp;
	}

	@Override
	public AbilityList getAbilities() {
		return abilities.shallowCopy();
	}

	@Override
	public BasicStats getBasicStats() {
		return basicStats;
	}

}
