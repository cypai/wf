package com.pipai.wf.artemis.system.battleanimation;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pipai.wf.artemis.event.AiNextEvent;
import com.pipai.wf.artemis.event.AnimationEndEvent;
import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.artemis.system.battle.BattleSystem;
import com.pipai.wf.artemis.system.input.BattleKeysInputProcessorSystem;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.event.BattleEvent;

import net.mostlyoriginal.api.event.common.EventSystem;
import net.mostlyoriginal.api.event.common.Subscribe;

public class AnimationHandlerControllerSystem extends NoProcessingSystem {

	private static final Logger LOGGER = LoggerFactory.getLogger(AnimationHandlerControllerSystem.class);

	private EventSystem eventSystem;

	private BattleKeysInputProcessorSystem keysInputSystem;
	private BattleSystem battleSystem;

	private LinkedList<BattleEvent> currentEvents;

	public AnimationHandlerControllerSystem() {
		currentEvents = new LinkedList<BattleEvent>();
	}

	public void animateBattleEvent(BattleEvent event) {
		LOGGER.debug("Received animation request for: {}", event);
		keysInputSystem.disable();
		currentEvents.push(event);
		eventSystem.dispatch(event);
	}

	@Subscribe
	public void handleAnimationEnd(AnimationEndEvent event) {
		LOGGER.debug("Received animation end event");
		currentEvents.pop();
		if (currentEvents.isEmpty()) {
			keysInputSystem.enable();
			if (battleSystem.getBattleController().getCurrentTeam().equals(Team.ENEMY)) {
				eventSystem.dispatch(new AiNextEvent());
			}
		}
	}

	public BattleEvent getCurrentEvent() {
		return currentEvents.peek();
	}

}
