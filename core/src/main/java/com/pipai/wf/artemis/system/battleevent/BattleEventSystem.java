package com.pipai.wf.artemis.system.battleevent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.battle.event.BattleEvent;

import net.mostlyoriginal.api.event.common.Subscribe;

public class BattleEventSystem extends NoProcessingSystem {

	private static final Logger LOGGER = LoggerFactory.getLogger(BattleEventSystem.class);

	@Subscribe
	public void processBattleEvent(BattleEvent event) {
		LOGGER.debug("Recieved battle log event: " + event.getClass());
	}

}
