package com.pipai.wf.battle.log;

public interface BattleEventLoggable {

	/**
	 * Takes in either a BattleLog or null.
	 * If parameter is null, it removes the log.
	 */
	public void register(BattleLog log);

}
