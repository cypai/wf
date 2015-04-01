package com.pipai.wf.battle.agent;

import java.util.ArrayList;
import java.util.LinkedList;

import com.pipai.wf.battle.attack.Attack;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.log.BattleEventLoggable;
import com.pipai.wf.battle.log.BattleLog;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.BattleMapCell;
import com.pipai.wf.battle.map.GridPosition;

public class Agent implements BattleEventLoggable {
	
	public enum State {NEUTRAL, KO, OVERWATCH};
	public enum Team {PLAYER, ENEMY}
	
	protected Team team;
	protected int maxHP, maxAP, hp, ap;
	protected int mobility;
	protected State state;
	protected BattleMap map;
	protected GridPosition position;
	protected BattleLog log;
	protected Attack overwatchAttack;
	
	public Agent(BattleMap map, AgentState state) {
		this.map = map;
		team = state.team;
		position = state.position;
		maxHP = state.maxHP;
		hp = state.hp;
		maxAP = state.maxAP;
		ap = state.ap;
		mobility = state.mobility;
	}
	
	public Team getTeam() { return this.team; }
	public void setTeam(Team team) { this.team = team; }
	public int getAP() { return this.ap; }
	public void setAP(int ap) { this.ap = ap; }
	public int getMaxAP() { return this.maxAP; }
	public int getHP() { return this.hp; }
	public void setHP(int hp) {
		this.hp = hp;
		if (this.hp <= 0) {
			this.hp = 0;
			this.state = State.KO;
			this.map.getCell(this.position).makeAgentInactive();
		} else {
			this.state = State.NEUTRAL;
		}
	}
	public void decrementHP(int amt) { this.setHP(this.getHP() - amt); }
	public int getMaxHP() { return this.maxHP; }
	public int getMobility() { return this.mobility; }
	public boolean isKO() { return this.state == State.KO; }
	public boolean isOverwatching() { return this.state == State.OVERWATCH; }
	
	public GridPosition getPosition() { return this.position; }
	
	private void setPosition(GridPosition pos) {
		this.map.getCell(this.position).removeAgent();
		this.map.getCell(pos).setAgent(this);
		this.position = pos;
	}
	
	public boolean canSee(Agent other) {
		return this.map.lineOfSight(this.getPosition(), other.getPosition());
	}
	
	public boolean inRange(Agent other) {
		return other.canSee(this);
	}
	
	public ArrayList<Agent> enemiesInRange() {
		ArrayList<Agent> l = new ArrayList<Agent>();
		for (Agent a : this.map.getAgents()) {
			if (a.team != this.team && this.canSee(a)) {
				l.add(a);
			}
		}
		return l;
	}
	
	public void move(LinkedList<GridPosition> path) {
		boolean isValid = true;
		BattleEvent event = BattleEvent.moveEvent(this, path);
		for (GridPosition pos : path) {
			if (pos.equals(this.getPosition())) { continue; }
			BattleMapCell cell = this.map.getCell(pos);
			if (cell == null || !cell.isEmpty()) {
				isValid = false;
				break;
			}
		}
		if (isValid) {
			logEvent(event);
		} else {
			return;
		}
		for (GridPosition pos : path) {
			this.setPosition(pos);
			for (Agent a : this.enemiesInRange()) {
				if (a.isOverwatching()) {
					a.activateOverwatch(this, event);
					if (this.isKO()) {
						return;
					}
				}
			}
		}
		GridPosition dest = path.peekLast();
		this.setPosition(dest);
		this.setAP(this.ap - 1);
	}
	
	public void rangeAttack(Agent other, Attack attack) {
		float distance = 0;
		boolean hit = attack.rollToHit(distance);
		int dmg = 0;
		if (hit) {
			dmg = attack.damageRoll(distance);
			other.decrementHP(dmg);
		}
		this.setAP(0);
		logEvent(BattleEvent.attackEvent(this, other, attack, hit, dmg));
	}
	
	public void overwatch(Attack attack) {
		this.overwatchAttack = attack;
		this.state = State.OVERWATCH;
		this.setAP(0);
		logEvent(BattleEvent.overwatchEvent(this, attack));
	}
	
	public void activateOverwatch(Agent other, BattleEvent activationLogEvent) {
		float distance = 0;
		boolean hit = overwatchAttack.rollToHit(distance);
		this.state = State.NEUTRAL;
		int dmg = 0;
		if (hit) {
			dmg = overwatchAttack.damageRoll(distance);
			other.decrementHP(dmg);
		}
		activationLogEvent.addChainEvent(BattleEvent.overwatchActivationEvent(this, other, overwatchAttack, hit, dmg));
	}

	@Override
	public void register(BattleLog log) {
		this.log = log;
	}
	
	private void logEvent(BattleEvent ev) {
		if (log != null) {
			log.logEvent(ev);
		}
	}
	
}
