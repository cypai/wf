package com.pipai.wf.save.variables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.pipai.wf.save.CorruptedSaveException;
import com.pipai.wf.save.SaveComponent;
import com.pipai.wf.save.SaveHeader;
import com.pipai.wf.save.utils.SaveUtils;

public class SaveVariablesComponent implements SaveComponent {

	public static final SaveHeader HEADER = new SaveHeader("Variables");

	private HashMap<String, String> variables;

	public SaveVariablesComponent() {
		variables = new HashMap<>();
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

	@Override
	public String serialize() {
		StringBuilder builder = new StringBuilder();
		builder.append(HEADER + "\n");
		for (Map.Entry<String, String> entry : variables.entrySet()) {
			builder.append(entry.getKey() + " = " + entry.getValue() + "\n");
		}
		return builder.toString();
	}

	@Override
	public void deserialize(String rawData) throws CorruptedSaveException {
		ArrayList<String> rawVariableInfo = SaveUtils.getLinesUnderHeader(HEADER, rawData);
		for (String line : rawVariableInfo) {
			if (line.trim().isEmpty()) {
				continue;
			}
			String[] tokens = line.split("=");
			if (tokens.length != 2) {
				throw new CorruptedSaveException("Save variable line does not have 1 equal sign");
			}
			String name = tokens[0].trim();
			String value = tokens[1].trim();
			variables.put(name, value);
		}
	}

}
