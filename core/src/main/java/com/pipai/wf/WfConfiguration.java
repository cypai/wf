package com.pipai.wf;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class WfConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(WfConfiguration.class);

	private static final String SCENARIO = "scenario";

	private final Properties properties;

	public WfConfiguration() {
		properties = new Properties();
		try {
			FileHandle configFile = Gdx.files.local("config/wf.properties");
			properties.load(configFile.reader());
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}

	public String getScenario() {
		return properties.getProperty(SCENARIO);
	}

}
