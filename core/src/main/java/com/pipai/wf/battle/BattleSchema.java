package com.pipai.wf.battle;

import java.util.List;
import java.util.stream.Collectors;

import com.pipai.wf.unit.schema.ImmutableUnitSchema;
import com.pipai.wf.unit.schema.UnitSchema;

public class BattleSchema {

	private List<ImmutableUnitSchema> partySchemas;

	public BattleSchema(List<? extends UnitSchema> party) {
		this.partySchemas = party.stream()
				.map(ImmutableUnitSchema::new)
				.collect(Collectors.toList());
	}

	public List<ImmutableUnitSchema> getPartySchemas() {
		return partySchemas;
	}

}
