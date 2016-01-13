package com.pipai.wf.battle;

import com.pipai.wf.battle.event.BattleEvent;

public interface BattleObserver {

	void notifyBattleEvent(BattleEvent ev);

}
