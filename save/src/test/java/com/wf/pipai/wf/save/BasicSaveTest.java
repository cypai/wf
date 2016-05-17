package com.wf.pipai.wf.save;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.pipai.libgdx.test.GdxMockedTest;
import com.pipai.wf.save.BasicSave;
import com.pipai.wf.save.CorruptedSaveException;

public class BasicSaveTest extends GdxMockedTest {

	public static final FileHandle SAVE_FILE = Gdx.files.local("test-save.txt");

	@After
	public void teardown() {
		SAVE_FILE.delete();
	}

	@Test
	public void saveVariableTest() {
		BasicSave save = new BasicSave();
		final String scenario = "scenario";
		Assert.assertFalse(save.getVariable(scenario).isPresent());
		final String scenarioValue = "test_scenario.txt";
		save.setVariable(scenario, scenarioValue);
		Assert.assertEquals(scenarioValue, save.getVariable(scenario).get());
	}

	@Test
	public void saveWriteVariablesTest() throws URISyntaxException, CorruptedSaveException, IOException {
		BasicSave save = new BasicSave();
		final String scenario = "scenario";
		final String scenarioValue = "test.txt";
		final String label = "label";
		final String labelValue = "opening";
		save.setVariable(scenario, scenarioValue);
		save.setVariable(label, labelValue);
		save.save(SAVE_FILE);
		BasicSave loadSave = new BasicSave();
		loadSave.load(SAVE_FILE);
		Assert.assertEquals(scenarioValue, loadSave.getVariable(scenario).get());
		Assert.assertEquals(labelValue, loadSave.getVariable(label).get());
	}

}
