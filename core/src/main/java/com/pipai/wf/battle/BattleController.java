package com.pipai.wf.battle;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.log.BattleLog;
import com.pipai.wf.battle.map.BattleMap;

/*
 * Controls the flow of battle as an interface between the Player/AIs
 */

public class BattleController {
	
	private BattleMap map;
	private BattleLog log;
	private int currentTeam, maxTeams;	//Turn system variables
	
	public BattleController(BattleMap map) {
		log = new BattleLog();
		this.map = map;
		this.map.register(log);
		this.currentTeam = 0;
		this.maxTeams = 2;
	}
	
	public BattleMap getBattleMap() {
		return this.map;
	}
	
	public int getCurrentTeam() {
		return this.currentTeam;
	}
	
	public void endTurn() {
		this.currentTeam = (this.currentTeam + 1) % this.maxTeams;
	}
	
	public BattleEvent performAction(Action a) {
		log.clear();
		a.perform();
		return log.getLastEvent();
	}
	
	public BattleLog getLog() {
		return log;
	}
	
}