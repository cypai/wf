package com.pipai.wf.battle;

import java.util.List;

import com.pipai.wf.unit.schema.UnitSchema;

public class BattleSchema {

	private List<UnitSchema> party;

	public BattleSchema(List<UnitSchema> party) {
		this.party = party;
	}

	public List<UnitSchema> getPartySchemas() {
		return party;
	}

}
