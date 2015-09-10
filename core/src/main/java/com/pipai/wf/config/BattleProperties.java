package com.pipai.wf.config;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class BattleProperties {

	private static final Logger logger = LoggerFactory.getLogger(BattleProperties.class);

	private final Properties battleConfig = new Properties();

	public BattleProperties() {
    	try {
    		FileHandle configFile = Gdx.files.local("config/battle.properties");
			battleConfig.load(configFile.reader());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private int getInt(String key, int defaultVal) {
		String result = battleConfig.getProperty(key);
		return result == null ? defaultVal : Integer.parseInt(result);
	}

	public int sightRange() {
		return getInt("RANGE", 17);
	}

	public float sightRangeAdjusted() {
		return sightRange() + (float)Math.sqrt(2)/2.0f;
	}

}
