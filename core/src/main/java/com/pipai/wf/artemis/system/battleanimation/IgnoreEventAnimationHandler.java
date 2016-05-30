package com.pipai.wf.artemis.system.battleanimation;

import com.google.common.collect.ImmutableList;
import com.pipai.wf.artemis.event.AnimationEndEvent;
import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.battle.event.BattleEvent;
import com.pipai.wf.battle.event.StartTurnEvent;

import net.mostlyoriginal.api.event.common.EventSystem;
import net.mostlyoriginal.api.event.common.Subscribe;

public class IgnoreEventAnimationHandler extends NoProcessingSystem {

	public static final ImmutableList<Class<? extends BattleEvent>> IGNORE_LIST = ImmutableList
			.<Class<? extends BattleEvent>>builder()
			.add(StartTurnEvent.class)
			.build();

	private EventSystem eventSystem;

	@Subscribe
	public void handleBattleEvent(BattleEvent event) {
		if (IGNORE_LIST.contains(event.getClass())) {
			eventSystem.dispatch(new AnimationEndEvent());
		}
	}

}
