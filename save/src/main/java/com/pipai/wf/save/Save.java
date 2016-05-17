package com.pipai.wf.save;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.files.FileHandle;

public abstract class Save {

	private static final Logger LOGGER = LoggerFactory.getLogger(Save.class);

	abstract List<SaveComponent> components();

	public void save(FileHandle file) throws IOException {
		StringBuilder saveData = new StringBuilder();
		for (SaveComponent component : components()) {
			LOGGER.debug("Saving component: " + component.getClass().getSimpleName());
			saveData.append(component.serialize());
		}
		try (OutputStream ostream = file.write(false)) {
			ostream.write(saveData.toString().getBytes("UTF-8"));
			ostream.close();
		}
	}

	public void load(FileHandle file) throws CorruptedSaveException {
		String rawSaveData = file.readString();
		for (SaveComponent component : components()) {
			LOGGER.debug("Loading component: " + component.getClass().getSimpleName());
			component.deserialize(rawSaveData);
		}
	}

}
