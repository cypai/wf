package com.pipai.wf.battle;

import com.pipai.wf.battle.map.BattleMap;

public class Battle {

	private BattleMap map;
	private BattleConfiguration battleConfig;
	private BattleController battleController;

	public Battle(BattleMap map, BattleConfiguration config) {
		this.map = map;
		battleConfig = config;
		battleController = new BattleController(map, battleConfig);
	}

	public BattleMap getBattleMap() {
		return map;
	}

	public BattleConfiguration getBattleConfiguration() {
		return battleConfig;
	}

	public BattleController getBattleController() {
		return battleController;
	}

}
