package com.pipai.wf.battle.agent;

import com.pipai.wf.battle.attack.Attack;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;

public class Agent {
	
	public enum State {NEUTRAL, KO};
	public enum Team {PLAYER, ENEMY}
	
	protected Team team;
	protected int maxHP, maxAP, hp, ap;
	protected int mobility;
	protected State state;
	protected BattleMap map;
	protected GridPosition position;
	
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
	
	public GridPosition getPosition() { return this.position; }
	
	public void move(GridPosition pos) {
		if (this.map.getCell(pos) != null) {
			if (this.map.getCell(pos).isEmpty()) {
				this.map.getCell(this.position).removeAgent();
				//Map Call: Generate curve from current to new position for overwatch check
				//Map Call: Check various points along the curve for overwatch, perform animation
				this.map.getCell(pos).setAgent(this);
				this.position = pos;
				this.setAP(this.ap - 1);
			}
		}
	}
	
	public void rangeAttack(Agent other, Attack attack) {
		if (attack.rollToHit(0)) {
			other.decrementHP(attack.damageRoll());
		}
		this.setAP(0);
	}
	
	
}
