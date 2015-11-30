package com.pipai.wf.battle;

import java.util.LinkedList;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.log.BattleLog;
import com.pipai.wf.battle.map.BattleMap;

/*
 * Controls the flow of battle as an interface between the Player/AIs
 */

public class BattleController {

	private BattleMap map;
	private BattleLog log;
	private Team currentTeam;
	private LinkedList<BattleObserver> observerList;
	private LinkedList<Agent> playerList, enemyList;
	private BattleConfiguration config;

	public BattleController(BattleMap map, BattleConfiguration config) {
		log = new BattleLog();
		this.map = map;
		currentTeam = Team.PLAYER;
		observerList = new LinkedList<BattleObserver>();
		playerList = new LinkedList<Agent>();
		enemyList = new LinkedList<Agent>();
		this.config = config;

		for (Agent a : this.map.getAgents()) {
			if (a.getTeam() == Team.PLAYER) {
				playerList.add(a);
			} else {
				enemyList.add(a);
			}
		}
	}

	public BattleConfiguration getBattleConfiguration() {
		return config;
	}

	public BattleMap getBattleMap() {
		return map;
	}

	public Team getCurrentTeam() {
		return currentTeam;
	}

	public void registerObserver(BattleObserver o) {
		observerList.add(o);
	}

	private void notifyObservers(BattleEvent ev) {
		for (BattleObserver o : observerList) {
			o.notifyBattleEvent(ev);
		}
	}

	public void endTurn() {
		if (currentTeam == Team.PLAYER) {
			for (Agent a : playerList) {
				a.onTurnEnd();
			}
			currentTeam = Team.ENEMY;
			for (Agent a : enemyList) {
				a.onTurnBegin();
			}
			notifyObservers(BattleEvent.startTurnEvent(Team.ENEMY));
		} else {
			// Round ended
			for (Agent a : enemyList) {
				a.onTurnEnd();
			}
			for (Agent a : map.getAgents()) {
				a.onRoundEnd();
			}
			// Start Player turn
			currentTeam = Team.PLAYER;
			for (Agent a : playerList) {
				a.onTurnBegin();
			}
			notifyObservers(BattleEvent.startTurnEvent(Team.PLAYER));
		}
	}

	public void performPostActionNotifications() {
		notifyObservers(log.getLastEvent());
	}

	public BattleResult battleResult() {
		int ko_amount = 0;
		for (Agent a : enemyList) {
			if (a.isKO()) {
				ko_amount += 1;
			}
		}
		if (ko_amount == enemyList.size()) {
			return new BattleResult(BattleResult.Result.VICTORY, playerList);
		}
		ko_amount = 0;
		for (Agent a : playerList) {
			if (a.isKO()) {
				ko_amount += 1;
			}
		}
		if (ko_amount == playerList.size()) {
			return new BattleResult(BattleResult.Result.DEFEAT, playerList);
		}
		return new BattleResult(BattleResult.Result.NONE, playerList);
	}

	public BattleLog getLog() {
		return log;
	}

}