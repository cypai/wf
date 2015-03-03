package com.pipai.wf.battle;

import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;

public class Agent {
	
	public enum State {NEUTRAL, KO};
	public enum Team {PLAYER, ENEMY}
	
	protected Team team;
	protected int ap;
	protected int mobility;
	protected BattleMap map;
	protected GridPosition position;
	
	public Agent(BattleMap map, Team team, GridPosition pos) {
		this.map = map;
		this.team = team;
		this.position = pos;
	}
	
	public Team getTeam() { return this.team; }
	protected void setTeam(Team team) { this.team = team; }
	public int getAP() { return this.ap; }
	protected void setAP(int ap) { this.ap = ap; }
	
	public GridPosition getPosition() { return this.position; }
	
	public void move(GridPosition pos) {
		this.map.getCell(this.position).removeAgent();
		//Map Call: Generate curve from current to new position for overwatch check
		//Map Call: Check various points along the curve for overwatch, perform animation
		this.map.getCell(pos).setAgent(this);
		this.setAP(this.ap - 1);
	}
	
	
}
