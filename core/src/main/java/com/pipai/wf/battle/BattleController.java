package com.pipai.wf.battle;

import java.util.LinkedList;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.log.BattleLog;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.exception.IllegalActionException;

/*
 * Controls the flow of battle as an interface between the Player/AIs
 */

public class BattleController {
	
	public enum Result {
		NONE, VICTORY, DEFEAT;
	}
	
	private BattleMap map;
	private BattleLog log;
	private Team currentTeam;
	private LinkedList<BattleObserver> observerList;
	private LinkedList<Agent> playerList, enemyList;
	
	public BattleController(BattleMap map) {
		log = new BattleLog();
		this.map = map;
		this.map.register(log);
		this.currentTeam = Team.PLAYER;
		this.observerList = new LinkedList<BattleObserver>();
		this.playerList = new LinkedList<Agent>();
		this.enemyList = new LinkedList<Agent>();

		for (Agent a : this.map.getAgents()) {
			if (a.getTeam() == Team.PLAYER) {
				this.playerList.add(a);
			} else {
				this.enemyList.add(a);
			}
		}
	}
	
	public BattleMap getBattleMap() {
		return this.map;
	}
	
	public Team getCurrentTeam() {
		return this.currentTeam;
	}
	
	public void registerObserver(BattleObserver o) {
		this.observerList.add(o);
	}
	
	private void notifyObservers(BattleEvent ev) {
		for (BattleObserver o : this.observerList) {
			o.notifyBattleEvent(ev);
		}
	}
	
	public void endTurn() {
		if (this.currentTeam == Team.PLAYER) {
			this.currentTeam = Team.ENEMY;
			for (Agent a : this.enemyList) {
				a.postTurnReset();
			}
			notifyObservers(BattleEvent.startTurnEvent(Team.ENEMY));
		} else {
			this.currentTeam = Team.PLAYER;
			for (Agent a : this.playerList) {
				a.postTurnReset();
			}
			notifyObservers(BattleEvent.startTurnEvent(Team.PLAYER));
		}
	}
	
	public void performAction(Action a) throws IllegalActionException {
		log.clear();
		a.perform();
		this.notifyObservers(log.getLastEvent());
	}
	
	public Result battleResult() {
		int ko_amount = 0;
		for (Agent a : enemyList) {
			if (a.isKO()) {
				ko_amount += 1;
			}
		}
		if (ko_amount == enemyList.size()) {
			return Result.VICTORY;
		}
		ko_amount = 0;
		for (Agent a : playerList) {
			if (a.isKO()) {
				ko_amount += 1;
			}
		}
		if (ko_amount == playerList.size()) {
			return Result.DEFEAT;
		}
		return Result.NONE;
	}
	
	public BattleLog getLog() {
		return log;
	}
	
}