package com.pipai.wf.battle.action;

import com.pipai.wf.battle.Agent;
import com.pipai.wf.battle.map.GridPosition;

public class MoveAction extends Action {
	
	protected GridPosition newPos;
	
	public MoveAction(Agent performerAgent, GridPosition pos) {
		super(performerAgent);
		this.newPos = pos;
	}
	
	public void perform() {
		super.perform();
		this.performerAgent.move(this.newPos);
	}

	public int getAPRequired() { return 1; }
	
}
