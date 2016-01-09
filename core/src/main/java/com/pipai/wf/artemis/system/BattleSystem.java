package com.pipai.wf.artemis.system;

import com.artemis.BaseSystem;
import com.pipai.wf.battle.Battle;
import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.map.BattleMap;

/**
 * Acts as a "singleton" for getting battle-related objects
 */
public class BattleSystem extends BaseSystem {

	private final Battle battle;

	public BattleSystem(Battle battle) {
		this.battle = battle;
	}

	public BattleMap getBattleMap() {
		return battle.getBattleMap();
	}

	public BattleController getBattleController() {
		return battle.getBattleController();
	}

	public BattleConfiguration getBattleConfiguration() {
		return battle.getBattleConfiguration();
	}

	@Override
	protected boolean checkProcessing() {
		return false;
	}

	@Override
	protected void processSystem() {
		// Do nothing
	}

}
