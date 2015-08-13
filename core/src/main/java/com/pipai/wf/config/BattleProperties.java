package com.pipai.wf.config;

import java.io.IOException;
import java.util.Properties;

import com.badlogic.gdx.Gdx;

public class BattleProperties {
	
	private final Properties battleConfig = new Properties();
	
	public BattleProperties() {
    	try {
			battleConfig.load(Gdx.files.internal("battle.properties").reader());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private int getInt(String key, int defaultVal) {
		String result = battleConfig.getProperty(key);
		return result == null ? defaultVal : Integer.parseInt(result);
	}
	
	public int sightRange() {
		return getInt("RANGE", 17);
	}
	
}
