package com.pipai.wf.battle;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.map.BattleMap;

/*
 * Controls the flow of battle as an interface between the Player/AIs
 */

public class BattleController {
	
	private BattleMap map;
	private int currentTeam, maxTeams;	//Turn system variables
	
	public BattleController(BattleMap map) {
		this.map = map;
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
	
	public void performAction(Action a) {
		a.perform();
	}
	
}