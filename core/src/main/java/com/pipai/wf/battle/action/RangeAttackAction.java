package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.attack.Attack;

public class RangeAttackAction extends Action {
	
	protected Agent target;
	protected Attack attack;
	
	public RangeAttackAction(Agent performerAgent, Agent target, Attack attack) {
		super(performerAgent);
		this.target = target;
		this.attack = attack;
	}
	
	public void perform() {
		super.perform();
		this.performerAgent.rangeAttack(this.target, this.attack);
	}

	public int getAPRequired() { return 1; }
	
}
