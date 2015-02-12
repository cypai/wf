package com.pipai.wf.battle.action;

import com.pipai.wf.battle.Agent;
import com.pipai.wf.battle.BattleMap;

public abstract class Action {
	
	protected Agent performerAgent;
	protected BattleMap map;
	
	public Action(Agent performerAgent, BattleMap map) {
		this.performerAgent = performerAgent;
		this.map = map;
	}
	
	public abstract void perform();
	
	public abstract int getAPRequired();
	
}
