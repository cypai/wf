package com.pipai.wf.save;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.pipai.wf.unit.schema.MutableUnitSchema;
import com.pipai.wf.unit.schema.UnitSchema;

public class Save {

	private List<MutableUnitSchema> party;
	private HashMap<String, String> variables;

	public Save() {
		party = new ArrayList<>();
		variables = new HashMap<>();
	}

	public List<MutableUnitSchema> getParty() {
		return party;
	}

	public void setParty(List<? extends UnitSchema> party) {
		this.party = party.stream()
				.map(MutableUnitSchema::new)
				.collect(Collectors.toList());
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
