package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.attack.Attack;
import com.pipai.wf.exception.IllegalActionException;

public class RangeAttackAction extends Action {
	
	protected Agent target;
	protected Attack attack;
	
	public RangeAttackAction(Agent performerAgent, Agent target, Attack attack) {
		super(performerAgent);
		this.target = target;
		this.attack = attack;
	}
	
	public void perform() throws IllegalActionException {
		super.perform();
		getPerformer().rangeAttack(this.target, this.attack);
	}

	public int getAPRequired() { return 1; }
	
}
