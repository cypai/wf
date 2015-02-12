package com.pipai.wf.battle;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.BattleMapCell;

public class Agent {
	
	public enum State {NEUTRAL, KO};
	
	protected int ap;
	protected int mobility;
	protected BattleMap map;
	protected BattleMapCell containingCell;
	
	public Agent(BattleMap map) {
		this.map = map;
	}
	
	public int getAP() { return this.ap; }
	public void setAP(int ap) { this.ap = ap; }
	
	public void setCell(BattleMapCell cell) {
		this.containingCell = cell;
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
