package com.pipai.wf.save;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import com.pipai.wf.unit.schema.UnitSchema;

public class SaveWriter {

	private Save save;

	private StringBuilder saveBuilder;

	public SaveWriter(Save save) {
		this.save = save;
	}

	public void save(FileHandle file) throws IOException {
		saveBuilder = new StringBuilder();
		buildHeader(saveBuilder);
		buildSaveVariables(saveBuilder);
		buildPartySave(saveBuilder);
		String saveData = saveBuilder.toString();
		try (OutputStream ostream = file.write(false)) {
			ostream.write(saveData.getBytes());
			ostream.close();
		}
	}

	private void buildHeader(StringBuilder builder) {
		saveBuilder.append("WF Save File\n\n");
	}

	private void buildSaveVariables(StringBuilder builder) {
		saveBuilder.append(SaveHeader.VARIABLES.toString() + "\n");
		for (Map.Entry<String, String> entry : save.getVariables().entrySet()) {
			builder.append(entry.getKey() + " = " + entry.getValue() + "\n");
		}
	}

	private void buildPartySave(StringBuilder builder) {
		UnitSchemaSaveConverter converter = new UnitSchemaSaveConverter();
		saveBuilder.append(SaveHeader.PARTY.toString() + "\n");
		for (UnitSchema schema : save.getParty()) {
			saveBuilder.append(converter.transformUnitSchemaToString(schema));
			saveBuilder.append('\n');
		}
	}

}
