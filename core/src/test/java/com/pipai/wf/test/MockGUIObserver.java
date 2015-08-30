package com.pipai.wf.test;

import com.pipai.wf.battle.BattleObserver;
import com.pipai.wf.battle.log.BattleEvent;

public class MockGUIObserver implements BattleObserver {

	public BattleEvent ev;

	@Override
	public void notifyBattleEvent(BattleEvent ev) {
		this.ev = ev;
	}

}
