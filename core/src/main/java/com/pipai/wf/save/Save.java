package com.pipai.wf.save;

import java.util.ArrayList;
import java.util.List;

import com.pipai.wf.unit.schema.UnitSchema;

public class Save {

	private List<UnitSchema> party;

	public Save() {
		party = new ArrayList<>();
	}

	public List<UnitSchema> getParty() {
		return party;
	}

	public void setParty(List<UnitSchema> party) {
		this.party = party;
	}

}
