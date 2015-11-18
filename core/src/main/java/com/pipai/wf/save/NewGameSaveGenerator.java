package com.pipai.wf.save;

import java.util.ArrayList;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.unit.race.Race;
import com.pipai.wf.unit.schema.FlameFairySchema;
import com.pipai.wf.unit.schema.RaceTemplateSchema;
import com.pipai.wf.unit.schema.TidusSchema;
import com.pipai.wf.unit.schema.UnitSchema;

public class NewGameSaveGenerator {

	private BattleConfiguration config;

	public NewGameSaveGenerator(BattleConfiguration config) {
		this.config = config;
	}

	public Save generateNewSave() {
		Save save = new Save();
		save.setParty(generateParty());
		return save;
	}

	private ArrayList<AgentState> generateParty() {
		ArrayList<UnitSchema> partySchema = new ArrayList<>();
		partySchema.add(new TidusSchema());	// Tidus
		partySchema.add(new RaceTemplateSchema(Race.HUMAN));	// Sienna
		partySchema.add(new RaceTemplateSchema(Race.FAIRY));	// Sapphire
		partySchema.add(new RaceTemplateSchema(Race.CAT));	// Mira
		partySchema.add(new FlameFairySchema());	// Sunny
		partySchema.add(new RaceTemplateSchema(Race.FOX));	// Nolan
		ArrayList<AgentState> party = new ArrayList<>();
		AgentStateFactory factory = new AgentStateFactory(config);
		for (UnitSchema schema : partySchema) {
			party.add(factory.createFromSchema(schema));
		}
		return party;
	}

}
