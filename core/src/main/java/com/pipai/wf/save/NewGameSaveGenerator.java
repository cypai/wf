package com.pipai.wf.save;

import java.util.ArrayList;

import com.pipai.wf.WfConfiguration;
import com.pipai.wf.unit.race.Race;
import com.pipai.wf.unit.schema.FlameFairySchema;
import com.pipai.wf.unit.schema.MutableUnitSchema;
import com.pipai.wf.unit.schema.RaceTemplateSchema;
import com.pipai.wf.unit.schema.TidusSchema;

public class NewGameSaveGenerator {

	public Save generateNewSave() {
		Save save = new Save();
		save.setParty(generateParty());
		WfConfiguration config = new WfConfiguration();
		save.setVariable("scenario", config.getScenario());
		return save;
	}

	private static ArrayList<MutableUnitSchema> generateParty() {
		ArrayList<MutableUnitSchema> party = new ArrayList<>();
		party.add(new MutableUnitSchema(new TidusSchema()));
		party.add(new MutableUnitSchema(new RaceTemplateSchema(Race.HUMAN)));
		party.add(new MutableUnitSchema(new RaceTemplateSchema(Race.FAIRY)));
		party.add(new MutableUnitSchema(new RaceTemplateSchema(Race.CAT)));
		party.add(new MutableUnitSchema(new FlameFairySchema()));
		party.add(new MutableUnitSchema(new RaceTemplateSchema(Race.FOX)));
		return party;
	}

}
