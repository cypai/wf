package com.pipai.wf.artemis.system.battleanimation;

import java.util.Stack;

import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.battle.event.BattleEvent;

public class AnimationHandlerControllerSystem extends NoProcessingSystem {

	private Stack<BattleEvent> currentEvent;

	public AnimationHandlerControllerSystem() {
		currentEvent = new Stack<BattleEvent>();
	}

}
