package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.attack.Attack;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.exception.IllegalActionException;

public class RangeAttackAction extends TargetedAction {
	
	protected Attack attack;
	
	public RangeAttackAction(Agent performerAgent, Agent target, Attack attack) {
		super(performerAgent, target);
		this.attack = attack;
	}

	public int getAPRequired() { return 1; }

    @Override
    protected void performImpl() throws IllegalActionException {
        getPerformer().getCurrentWeapon().expendAmmo(attack.requiredAmmo());
        float distance = 0;
        boolean hit = attack.rollToHit(getPerformer(), getTarget(), distance);
        int dmg = 0;
        if (hit) {
            dmg = attack.damageRoll(getPerformer(), getTarget(), distance);
            getTarget().takeDamage(dmg);
        }
        getPerformer().setAP(0);
        log(BattleEvent.attackEvent(getPerformer(), getTarget(), attack, hit, dmg));
    }
	
}
