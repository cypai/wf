package com.pipai.wf.scenario;

import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Splitter;

public class VisualNovelScene {

	private List<String> scenarioLines;

	public VisualNovelScene(FileHandle scenarioFile) {
		scenarioLines = Splitter.on("\n").splitToList(scenarioFile.readString());
	}

	public List<String> getSceneLines() {
		return scenarioLines;
	}

}
