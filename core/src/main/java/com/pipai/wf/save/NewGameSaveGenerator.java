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
		party.add(new UnitSchema(new TidusSchema()));
		party.add(new UnitSchema(new RaceTemplateSchema(Race.HUMAN)));
		party.add(new UnitSchema(new RaceTemplateSchema(Race.FAIRY)));
		party.add(new UnitSchema(new RaceTemplateSchema(Race.CAT)));
		party.add(new UnitSchema(new FlameFairySchema()));
		party.add(new UnitSchema(new RaceTemplateSchema(Race.FOX)));
		return party;
	}

}
