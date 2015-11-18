package com.pipai.wf.save;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class SaveManager {

	private String path;

	public SaveManager(String path) {
		this.path = path;
	}

	public void save(Save save, int slot) throws IOException {
		SaveWriter writer = new SaveWriter(save);
		writer.save(generateSlotHandle(slot));
	}

	public Save load(int slot) {
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
		return Gdx.files.external(path + String.valueOf(slot) + ".txt");
	}

}
