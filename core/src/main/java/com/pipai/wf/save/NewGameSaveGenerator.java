package com.pipai.wf.save;

import java.util.ArrayList;

import com.pipai.wf.unit.race.Race;
import com.pipai.wf.unit.schema.FlameFairySchema;
import com.pipai.wf.unit.schema.RaceTemplateSchema;
import com.pipai.wf.unit.schema.TidusSchema;
import com.pipai.wf.unit.schema.UnitSchema;

public class NewGameSaveGenerator {

	public Save generateNewSave() {
		Save save = new Save();
		save.setParty(generateParty());
		return save;
	}

	private ArrayList<UnitSchema> generateParty() {
		ArrayList<UnitSchema> party = new ArrayList<>();
		party.add(new UnitSchema(new TidusSchema()));	// Tidus
		party.add(new UnitSchema(new RaceTemplateSchema(Race.HUMAN)));	// Sienna
		party.add(new UnitSchema(new RaceTemplateSchema(Race.FAIRY)));	// Sapphire
		party.add(new UnitSchema(new RaceTemplateSchema(Race.CAT)));	// Mira
		party.add(new UnitSchema(new FlameFairySchema()));	// Sunny
		party.add(new UnitSchema(new RaceTemplateSchema(Race.FOX)));	// Nolan
		return party;
	}

}
