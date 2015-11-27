package com.pipai.wf.save;

import java.util.ArrayList;

import com.pipai.wf.unit.schema.UnitSchema;

public class Save {

	private ArrayList<UnitSchema> partyInfo;

	public Save() {
		partyInfo = new ArrayList<>();
	}

	public ArrayList<UnitSchema> getParty() {
		return partyInfo;
	}

	public void setParty(ArrayList<UnitSchema> party) {
		partyInfo = party;
	}

}
