package com.pipai.wf.battle;

import com.pipai.wf.battle.log.BattleEvent;

public interface BattleObserver {

	public void notifyBattleEvent(BattleEvent ev);

}
