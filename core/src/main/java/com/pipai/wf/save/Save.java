package com.pipai.wf.save;

import java.util.ArrayList;

import com.pipai.wf.unit.schema.UnitSchema;

public class Save {

	private ArrayList<UnitSchema> party;

	public Save() {
		party = new ArrayList<>();
	}

	public ArrayList<UnitSchema> getParty() {
		return party;
	}

	public void setParty(ArrayList<UnitSchema> party) {
		this.party = party;
	}

}
