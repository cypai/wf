package com.pipai.wf.battle;

import com.pipai.wf.battle.map.BattleMap;

public class Battle {

	private BattleMap battleMap;
	private BattleConfiguration battleConfiguration;
	private BattleController battleController;

	public Battle(BattleMap map, BattleConfiguration config) {
		this.battleMap = map;
		battleConfiguration = config;
		battleController = new BattleController(map, battleConfiguration);
	}

	public BattleMap getBattleMap() {
		return battleMap;
	}

	public BattleConfiguration getBattleConfiguration() {
		return battleConfiguration;
	}

	public BattleController getBattleController() {
		return battleController;
	}

}
