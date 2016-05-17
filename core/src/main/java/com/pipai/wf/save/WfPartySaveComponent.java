package com.pipai.wf.save;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.pipai.wf.save.utils.SaveUtils;
import com.pipai.wf.unit.schema.MutableUnitSchema;
import com.pipai.wf.unit.schema.UnitSchema;

public class WfPartySaveComponent implements SaveComponent {

	public static final SaveHeader HEADER = new SaveHeader("Party");

	private List<MutableUnitSchema> party;

	public WfPartySaveComponent() {
		party = new ArrayList<>();
	}

	public List<MutableUnitSchema> getParty() {
		return party;
	}

	public void setParty(List<? extends UnitSchema> party) {
		this.party = party.stream()
				.map(MutableUnitSchema::new)
				.collect(Collectors.toList());
	}

	@Override
	public String serialize() {
		UnitSchemaSaveConverter converter = new UnitSchemaSaveConverter();
		StringBuilder saveBuilder = new StringBuilder();
		saveBuilder.append(HEADER + "\n");
		for (UnitSchema schema : party) {
			saveBuilder.append(converter.transformUnitSchemaToString(schema));
			saveBuilder.append('\n');
		}
		return saveBuilder.toString();
	}

	@Override
	public void deserialize(String rawData) throws CorruptedSaveException {
		UnitSchemaSaveConverter converter = new UnitSchemaSaveConverter();
		ArrayList<String> rawPartyInfo = SaveUtils.getLinesUnderHeader(HEADER, rawData);
		for (String line : rawPartyInfo) {
			party.add(new MutableUnitSchema(converter.parseStringRepresentation(line)));
		}
	}

}
