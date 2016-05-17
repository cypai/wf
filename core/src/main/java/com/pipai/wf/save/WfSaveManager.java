package com.pipai.wf.save;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class WfSaveManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(WfSaveManager.class);

	public static final String DEFAULT_DIRECTORY = "save/";

	private String path;

	public WfSaveManager() {
		this(DEFAULT_DIRECTORY);
	}

	public WfSaveManager(String path) {
		this.path = path;
	}

	public void save(WfSave save, int slot) throws IOException {
		FileHandle handle = generateSlotHandle(slot);
		LOGGER.debug("Saving to " + handle.file().getAbsolutePath());
		save.save(handle);
	}

	public WfSave load(int slot) throws CorruptedSaveException {
		WfSave save = new WfSave();
		FileHandle handle = generateSlotHandle(slot);
		LOGGER.debug("Loading from " + handle.file().getAbsolutePath());
		save.load(handle);
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
