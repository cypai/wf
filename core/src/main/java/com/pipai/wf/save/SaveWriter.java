package com.pipai.wf.save;

import java.io.IOException;
import java.io.OutputStream;

import com.badlogic.gdx.files.FileHandle;
import com.pipai.wf.battle.agent.AgentState;

public class SaveWriter {

	private Save save;

	private StringBuilder saveBuilder;

	public SaveWriter(Save save) {
		this.save = save;
	}

	public void write(SaveHeader header, String data) {
		saveBuilder.append(header.toString() + "\n");
		saveBuilder.append(data + "\n");
	}

	public void save(FileHandle file) throws IOException {
		saveBuilder = new StringBuilder();
		buildHeader(saveBuilder);
		buildPartySave(saveBuilder);
		String saveData = saveBuilder.toString();
		OutputStream ostream = file.write(false);
		ostream.write(saveData.getBytes());
		ostream.close();
	}

	private void buildHeader(StringBuilder builder) {
		saveBuilder.append("WF Save File\n\n");
	}

	private void buildPartySave(StringBuilder builder) {
		saveBuilder.append(SaveHeader.PARTY.toString() + "\n");
		for (AgentState as : save.getParty()) {
			saveBuilder.append(new AgentSaveRepresentation(save.getConfig(), as).getStringRepresentation());
			saveBuilder.append('\n');
		}
	}

}
