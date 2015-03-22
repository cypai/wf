package com.pipai.wf.battle.log;

import java.util.LinkedList;

public class BattleLog {
	
	public LinkedList<BattleEvent> log;
	
	public BattleLog() {
		log = new LinkedList<BattleEvent>();
	}
	
	public BattleEvent getLastEvent() {
		return log.peekLast();
	}
	
	public void logEvent(BattleEvent ev) {
		log.add(ev);
	}
	
	public void clear() {
		log.clear();
	}
	
}