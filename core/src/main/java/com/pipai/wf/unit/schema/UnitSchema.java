package com.pipai.wf.unit.schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.inventory.AgentInventory;
import com.pipai.wf.misc.BasicStats;
import com.pipai.wf.misc.HasBasicStats;
import com.pipai.wf.misc.HasName;
import com.pipai.wf.unit.ability.AbilityList;

/**
 * UnitSchema is the base class for storing unit-creation information. It also functions as a party state saving mechanism.
 *
 * This class will provide enough functionality to act the same as any subclasses during a load as long as the subclass follows
 * the functional interface and contract, since the JSON writer currently does not serialize UnitSchema subclass information.
 *
 * Although this class is mutable, subclasses might not be. Immutable subclasses should throw UnsupportedMethodException for setters.
 *
 */
// TODO: Make this an interface and move implementation to MutableUnitSchema
public class UnitSchema implements HasName, HasBasicStats {

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
	public UnitSchema(Agent agent) {
		name = agent.getName();
		basicStats = new BasicStats(agent.getHP(), agent.getMaxHP(), agent.getMaxMP(), agent.getMaxMP(), agent.getMaxAP(), agent.getMaxAP(),
				agent.getAim(), agent.getMobility(), agent.getDefense());
		abilities = agent.getAbilities().deepCopyAsNew();
		inventory = agent.getInventory();
		level = agent.getLevel();
		exp = agent.getExp();
		expGiven = agent.getExpGiven();
	}

	public UnitSchema(UnitSchema schema) {
		name = schema.getName();
		basicStats = schema.getBasicStats();
		abilities = schema.getAbilities();
		inventory = schema.getInventory();
		level = schema.getLevel();
		exp = schema.getExp();
		expGiven = schema.getExpGiven();
	}

	public UnitSchema(String name, BasicStats stats) {
		this.name = name;
		basicStats = stats;
		abilities = new AbilityList();
		// TODO: Magic number here...
		inventory = new AgentInventory(3);
	}

	@JsonCreator
	public UnitSchema(
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

	public AgentInventory getInventory() {
		return inventory;
	}

	public int getLevel() {
		return level;
	}

	public int getExpGiven() {
		return expGiven;
	}

	public int getExp() {
		return exp;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public AbilityList getAbilities() {
		return abilities.shallowCopy();
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
