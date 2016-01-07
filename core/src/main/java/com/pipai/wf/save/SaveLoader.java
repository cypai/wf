package com.pipai.wf.save;

import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.pipai.wf.unit.schema.UnitSchema;

public class SaveLoader {

	private Save save;

	public SaveLoader(Save save) {
		this.save = save;
	}

	public void load(FileHandle file) throws CorruptedSaveException {
		String rawSaveData = file.readString();
		ArrayList<UnitSchema> party = readPartySave(rawSaveData);
		save.setParty(party);
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
