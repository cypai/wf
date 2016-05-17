package com.pipai.wf.unit.schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.inventory.AgentInventory;
import com.pipai.wf.misc.BasicStats;
import com.pipai.wf.unit.ability.AbilityList;

public class ImmutableUnitSchema extends MutableUnitSchema {

	private static final String IMMUTABILITY_MSG = "ImmutableUnitSchemas are immutable";

	/**
	 * Creates a schema using the Agent's stats
	 */
	public ImmutableUnitSchema(Agent agent) {
		super(agent);
	}

	public ImmutableUnitSchema(UnitSchema schema) {
		super(schema);
	}

	/**
	 * A simple ImmutableUnitSchema. Defaults level to 1, exp to 0, and expGiven to 0.
	 */
	public ImmutableUnitSchema(String name, BasicStats stats) {
		super(name, stats);
	}

	@JsonCreator
	public ImmutableUnitSchema(
			@JsonProperty("name") String name,
			@JsonProperty("basicStats") BasicStats stats,
			@JsonProperty("abilities") AbilityList abilities,
			@JsonProperty("inventory") AgentInventory inventory,
			@JsonProperty("level") int level,
			@JsonProperty("exp") int exp,
			@JsonProperty("expGiven") int expGiven) {
		super(name, stats, abilities, inventory, level, exp, expGiven);
	}

	@Override
	public final void setLevel(int level) {
		throw new UnsupportedOperationException(IMMUTABILITY_MSG);
	}

	@Override
	public final void setExp(int exp) {
		throw new UnsupportedOperationException(IMMUTABILITY_MSG);
	}

}
