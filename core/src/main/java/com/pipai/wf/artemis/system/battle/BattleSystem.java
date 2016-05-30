package com.pipai.wf.artemis.system.battle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.artemis.system.battleanimation.AnimationHandlerControllerSystem;
import com.pipai.wf.battle.Battle;
import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.BattleObserver;
import com.pipai.wf.battle.event.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;

/**
 * Acts as a "singleton" for getting battle-related objects
 */
public class BattleSystem extends NoProcessingSystem implements BattleObserver {

	private static final Logger LOGGER = LoggerFactory.getLogger(BattleSystem.class);

	private AnimationHandlerControllerSystem animationController;

	private final Battle battle;

	public BattleSystem(Battle battle) {
		this.battle = battle;
		battle.getBattleController().registerObserver(this);
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
	public void notifyBattleEvent(BattleEvent ev) {
		LOGGER.debug("Notified of battle event of type " + ev.getClass().getSimpleName());
		animationController.animateBattleEvent(ev);
	}

}
