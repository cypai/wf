package com.pipai.wf.unit.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pipai.wf.battle.inventory.AgentInventory;
import com.pipai.wf.misc.HasBasicStats;
import com.pipai.wf.misc.HasName;
import com.pipai.wf.unit.ability.AbilityList;

/**
 * UnitSchema is the interface for storing unit-creation information. It also functions as a party state saving
 * mechanism.
 *
 * The JSON writer currently does not serialize UnitSchema subclass information.
 *
 * Immutable subclasses should throw UnsupportedMethodException for setters.
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public interface UnitSchema extends HasName, HasBasicStats {

	AgentInventory getInventory();

	int getLevel();

	int getExpGiven();

	int getExp();

	void setLevel(int level);

	void setExp(int exp);

	AbilityList getAbilities();

	@Override
	@JsonIgnore
	default int getHP() {
		return getBasicStats().getHP();
	}

	@Override
	@JsonIgnore
	default int getMaxHP() {
		return getBasicStats().getMaxHP();
	}

	@Override
	@JsonIgnore
	default int getMP() {
		return getBasicStats().getMP();
	}

	@Override
	@JsonIgnore
	default int getMaxMP() {
		return getBasicStats().getMaxMP();
	}

	@Override
	@JsonIgnore
	default int getAP() {
		return getBasicStats().getAP();
	}

	@Override
	@JsonIgnore
	default int getMaxAP() {
		return getBasicStats().getMaxAP();
	}

	@Override
	@JsonIgnore
	default int getAim() {
		return getBasicStats().getAim();
	}

	@Override
	@JsonIgnore
	default int getMobility() {
		return getBasicStats().getMobility();
	}

	@Override
	@JsonIgnore
	default int getDefense() {
		return getBasicStats().getDefense();
	}

}
