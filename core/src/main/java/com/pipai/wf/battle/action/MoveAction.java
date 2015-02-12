package com.pipai.wf.battle.action;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.Agent;
import com.pipai.wf.battle.BattleMap;

public class MoveAction extends Action {
	
	protected int new_x, new_y;
	
	public MoveAction(Agent performerAgent, BattleMap map, int new_x, int new_y) {
		super(performerAgent, map);
	}
	
	public void perform() {
	}

	public int getAPRequired() { return 1; }
	
}
