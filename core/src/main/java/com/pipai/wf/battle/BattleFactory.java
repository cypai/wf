package com.pipai.wf.battle;

import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.BattleMapGenerator;

public class BattleFactory {

	private BattleConfiguration config;

	public BattleFactory(BattleConfiguration config) {
		this.config = config;
	}

	public Battle build(BattleSchema schema) {
		BattleMapGenerator generator = new BattleMapGenerator(config);
		BattleMap map = generator.generateMap(schema);
		return new Battle(map, config);
	}

}
