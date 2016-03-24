package com.pipai.wf.save;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.files.FileHandle;
import com.pipai.wf.unit.schema.UnitSchema;

public class SaveLoader {

	private Save save;

	public SaveLoader(Save save) {
		this.save = save;
	}

	public void load(FileHandle file) throws CorruptedSaveException {
		String rawSaveData = file.readString();
		loadSaveVariables(rawSaveData);
		ArrayList<UnitSchema> party = readPartySave(rawSaveData);
		save.setParty(party);
	}

	private void loadSaveVariables(String rawSaveData) throws CorruptedSaveException {
		ArrayList<String> rawVariableInfo = getLinesUnderHeader(SaveHeader.VARIABLES, rawSaveData);
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
			save.setVariable(name, value);
		}
	}

	private static ArrayList<UnitSchema> readPartySave(String rawSaveData) throws CorruptedSaveException {
		UnitSchemaSaveConverter converter = new UnitSchemaSaveConverter();
		ArrayList<String> rawPartyInfo = getLinesUnderHeader(SaveHeader.PARTY, rawSaveData);
		ArrayList<UnitSchema> party = new ArrayList<>();
		for (String line : rawPartyInfo) {
			party.add(converter.parseStringRepresentation(line));
		}
		return party;
	}

	private static ArrayList<String> getLinesUnderHeader(SaveHeader header, String rawSaveData) {
		String[] lines = rawSaveData.split("\n");
		ArrayList<String> retLines = new ArrayList<>();
		boolean isUnderHeader = false;
		lines = Arrays.stream(lines).map(String::trim).toArray((size) -> new String[size]);
		for (String line : lines) {
			if (isHeader(line)) {
				if (SaveHeader.getHeader(line).equals(header)) {
					isUnderHeader = true;
				} else {
					isUnderHeader = false;
				}
				continue;
			}
			if (isUnderHeader) {
				retLines.add(line);
			}
		}
		return retLines;
	}

	private static boolean isHeader(String line) {
		return line.startsWith("[") && line.endsWith("]");
	}
}
