package com.pipai.wf.battle;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.BattleMapCell;
import com.pipai.wf.battle.map.Position;

public class Agent {
	
	public enum State {NEUTRAL, KO};
	
	protected int ap;
	protected int mobility;
	protected BattleMap map;
	protected BattleMapCell containingCell;
	protected Position position;
	
	public Agent(BattleMap map) {
		this.map = map;
	}
	
	public int getAP() { return this.ap; }
	public void setAP(int ap) { this.ap = ap; }
	
	public Position getPosition() { return this.position; }
	
	public void setCell(BattleMapCell cell, Position position) {
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
