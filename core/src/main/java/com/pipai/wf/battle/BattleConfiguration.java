package com.pipai.wf.battle;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.pipai.wf.battle.damage.DamageCalculator;
import com.pipai.wf.battle.damage.TargetedActionCalculator;
import com.pipai.wf.util.Rng;

public class BattleConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(BattleConfiguration.class);

	private final Properties battleProps;
	private final Rng rng;
	private final DamageCalculator damageCalculator;
	private final TargetedActionCalculator targetedActionCalculator;

	public BattleConfiguration() {
		battleProps = new Properties();
		rng = new Rng();
		damageCalculator = new DamageCalculator(this);
		targetedActionCalculator = new TargetedActionCalculator(this);
		try {
			FileHandle configFile = Gdx.files.local("config/battle.properties");
			battleProps.load(configFile.reader());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private int getInt(String key, int defaultVal) {
		String result = battleProps.getProperty(key);
		return result == null ? defaultVal : Integer.parseInt(result);
	}

	public int sightRange() {
		return getInt("RANGE", 17);
	}

	public float sightRangeAdjusted() {
		return sightRange() + (float) Math.sqrt(2) / 2.0f;
	}

	public float overwatchPenalty() {
		return 0.7f;
	}

	public float critDamageMultiplier() {
		return 1.5f;
	}

	public Rng getRng() {
		return rng;
	}

	public DamageCalculator getDamageCalculator() {
		return damageCalculator;
	}

	public TargetedActionCalculator getTargetedActionCalculator() {
		return targetedActionCalculator;
	}

}
