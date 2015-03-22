package com.pipai.wf.battle.action;

import java.util.LinkedList;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.GridPosition;

public class MoveAction extends Action {
	
	protected LinkedList<GridPosition> path;
	
	public MoveAction(Agent performerAgent, LinkedList<GridPosition> path) {
		super(performerAgent);
		this.path = path;
	}
	
	public void perform() {
		super.perform();
		this.performerAgent.move(this.path);
	}

	public int getAPRequired() { return 1; }
	
}
