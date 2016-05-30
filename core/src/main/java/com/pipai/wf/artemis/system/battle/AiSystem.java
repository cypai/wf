package com.pipai.wf.artemis.system.battle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.ComponentMapper;
import com.pipai.wf.artemis.components.TimerComponent;
import com.pipai.wf.artemis.event.TimerEvent;
import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.ai.Ai;
import com.pipai.wf.battle.ai.ModularAi;
import com.pipai.wf.battle.ai.utils.AiMoveRunnable;
import com.pipai.wf.battle.event.StartTurnEvent;

import net.mostlyoriginal.api.event.common.Subscribe;

public class AiSystem extends NoProcessingSystem {

	private static final Logger LOGGER = LoggerFactory.getLogger(AiSystem.class);

	private static final int AI_MOVE_WAIT_TIME = 60;

	private ComponentMapper<TimerComponent> mTimer;

	private BattleSystem battleSystem;

	private Ai ai;
	private AiMoveRunnable runAiThread;

	private Team currentTeam;

	private int key;

	@Override
	protected void initialize() {
		ai = new ModularAi(battleSystem.getBattleController());
	}

	@Subscribe
	public void handleStartTurnEvent(StartTurnEvent event) {
		LOGGER.debug("Received start turn event for team: " + event.team);
		currentTeam = event.team;
		if (currentTeam.equals(Team.ENEMY)) {
			ai.startTurn();
			int timerId = getWorld().create();
			TimerComponent cTimer = mTimer.create(timerId);
			cTimer.timer = AI_MOVE_WAIT_TIME;
			key = cTimer.key;
		}
	}

	@Subscribe
	public void handleAiTimerEvent(TimerEvent event) {
		if (event.key == key) {
			LOGGER.debug("AI running...");
			runAiThread = new AiMoveRunnable(ai);
			runAiThread.run();
		}
	}

}
