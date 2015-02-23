package com.pipai.wf.battle.action;

import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.Agent;
import com.pipai.wf.battle.map.GridPosition;

public class MoveAction extends Action {
	
	protected GridPosition newPos;
	
	public MoveAction(Agent performerAgent, BattleMap map, GridPosition pos) {
		super(performerAgent, map);
		this.newPos = pos;
	}
	
	public void perform() {
		super.perform();
		this.performerAgent.removeFromCell();
		this.map.getCell(this.newPos).setAgent(this.performerAgent);
	}

	public int getAPRequired() { return 1; }
	
}
