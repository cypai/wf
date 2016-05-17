package com.pipai.wf.test;

import com.pipai.wf.battle.BattleObserver;
import com.pipai.wf.battle.event.BattleEvent;

public class MockGUIObserver implements BattleObserver {

	private BattleEvent event;

	@Override
	public void notifyBattleEvent(BattleEvent ev) {
		event = ev;
	}

	public BattleEvent getEvent() {
		return event;
	}

}
