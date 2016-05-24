package com.pipai.wf.scenario;

import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Splitter;

public class VisualNovelScene {

	private List<String> sceneLines;

	public VisualNovelScene(FileHandle scenarioFile) {
		sceneLines = Splitter.on("\n").splitToList(scenarioFile.readString());
	}

	public List<String> getSceneLines() {
		return sceneLines;
	}

}
