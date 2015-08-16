package com.pipai.wf.battle.action;

import java.util.LinkedList;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.exception.IllegalActionException;

public class MoveAction extends Action {
	
	protected LinkedList<GridPosition> path;
	protected int useAP;
	
	public MoveAction(Agent performerAgent, LinkedList<GridPosition> path, int useAP) {
		super(performerAgent);
		this.path = path;
		this.useAP = useAP;
	}
	
	public void perform() throws IllegalActionException {
		super.perform();
		this.performerAgent.move(this.path, this.useAP);
	}

	public int getAPRequired() { return 1; }
	
}
