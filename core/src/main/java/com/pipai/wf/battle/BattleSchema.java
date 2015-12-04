package com.pipai.wf.battle;

import java.util.List;

import com.pipai.wf.unit.schema.UnitSchema;

public class BattleSchema {

	private List<UnitSchema> partySchemas;

	public BattleSchema(List<UnitSchema> party) {
		this.partySchemas = party;
	}

	public List<UnitSchema> getPartySchemas() {
		return partySchemas;
	}

}
