package com.pipai.wf.battle;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.log.BattleLog;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.exception.IllegalActionException;

/*
 * Controls the flow of battle as an interface between the Player/AIs
 */

public class BattleController {
	
	private BattleMap map;
	private BattleLog log;
	private Team currentTeam;
	
	public BattleController(BattleMap map) {
		log = new BattleLog();
		this.map = map;
		this.map.register(log);
		this.currentTeam = Team.PLAYER;
	}
	
	public BattleMap getBattleMap() {
		return this.map;
	}
	
	public Team getCurrentTeam() {
		return this.currentTeam;
	}
	
	public void endTurn() {
		if (this.currentTeam == Team.PLAYER) {
			this.currentTeam = Team.ENEMY;
		} else {
			this.currentTeam = Team.PLAYER;
		}
	}
	
	public BattleEvent performAction(Action a) throws IllegalActionException {
		log.clear();
		a.perform();
		return log.getLastEvent();
	}
	
	public BattleLog getLog() {
		return log;
	}
	
}