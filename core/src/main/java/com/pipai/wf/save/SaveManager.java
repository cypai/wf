package com.pipai.wf.save;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class SaveManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(SaveManager.class);

	public static final String DEFAULT_DIRECTORY = "save/";

	private String path;

	public SaveManager() {
		this(DEFAULT_DIRECTORY);
	}

	public SaveManager(String path) {
		this.path = path;
	}

	public void save(Save save, int slot) throws IOException {
		SaveWriter writer = new SaveWriter(save);
		FileHandle handle = generateSlotHandle(slot);
		LOGGER.debug("Saving to " + handle.file().getAbsolutePath());
		writer.save(handle);
	}

	public Save load(int slot) throws CorruptedSaveException {
		Save save = new Save();
		SaveLoader reader = new SaveLoader(save);
		reader.load(generateSlotHandle(slot));
		return save;
	}

	public void delete(int slot) {
		FileHandle f = generateSlotHandle(slot);
		f.delete();
	}

	private FileHandle generateSlotHandle(int slot) {
		return Gdx.files.local(path + String.valueOf(slot) + ".txt");
	}

}
