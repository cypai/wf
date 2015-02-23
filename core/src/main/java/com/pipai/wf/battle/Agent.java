package com.pipai.wf.battle;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.BattleMapCell;
import com.pipai.wf.battle.map.GridPosition;

public class Agent {
	
	public enum State {NEUTRAL, KO};
	public enum Team {PLAYER, ENEMY}
	
	protected Team team;
	protected int ap;
	protected int mobility;
	protected BattleMap map;
	protected BattleMapCell containingCell;
	protected GridPosition position;
	
	public Agent(BattleMap map, Team team) {
		this.map = map;
		this.team = team;
	}
	
	public Team getTeam() { return this.team; }
	public void setTeam(Team team) { this.team = team; }
	public int getAP() { return this.ap; }
	public void setAP(int ap) { this.ap = ap; }
	
	public GridPosition getPosition() { return this.position; }
	
	public void setCell(BattleMapCell cell, GridPosition position) {
		this.containingCell = cell;
		this.position = position;
	}
	
	public void removeFromCell() {
		if (this.containingCell != null) {
			BattleMapCell temp = this.containingCell;
			this.containingCell = null;
			temp.removeAgent();
		}
	}
	
	public void performAction(Action action) {
		action.perform();
	}
	
	
}
