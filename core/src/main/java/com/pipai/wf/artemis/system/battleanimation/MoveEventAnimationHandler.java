package com.pipai.wf.artemis.system.battleanimation;

import com.pipai.wf.battle.event.MoveEvent;

import net.mostlyoriginal.api.event.common.Subscribe;

public class MoveEventAnimationHandler extends BattleEventAnimationHandler {

	@Subscribe
	public void handleMoveEvent(MoveEvent event) {
		inputProcessingSystem.deactivateInput();
	}

}
