package com.pipai.wf.save;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.pipai.wf.unit.schema.UnitSchema;

public class Save {

	private List<UnitSchema> party;
	private HashMap<String, String> variables;

	public Save() {
		party = new ArrayList<>();
		variables = new HashMap<>();
	}

	public List<UnitSchema> getParty() {
		return party;
	}

	public void setParty(List<UnitSchema> party) {
		this.party = party;
	}

	public Optional<String> getVariable(String name) {
		return Optional.ofNullable(variables.get(name));
	}

	public void setVariable(String name, String value) {
		variables.put(name, value);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String> getVariables() {
		return (HashMap<String, String>) variables.clone();
	}

}
