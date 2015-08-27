package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.exception.IllegalActionException;

public abstract class TargetedAction extends Action {

    private Agent target;

    public TargetedAction(Agent performerAgent, Agent targetAgent) {
        super(performerAgent);
        target = targetAgent;
    }
    
    public Agent getTarget() {
        return target;
    }
    
    @Override
    public final void perform() throws IllegalActionException {
        super.perform();
        performImpl();
    }
    
    protected abstract void performImpl() throws IllegalActionException;

}
