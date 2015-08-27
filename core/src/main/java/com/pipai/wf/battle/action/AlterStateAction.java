package com.pipai.wf.battle.action;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.exception.IllegalActionException;

public abstract class AlterStateAction extends Action {

    public AlterStateAction(Agent performerAgent) {
        super(performerAgent);
    }
    
    @Override
    public final void perform() throws IllegalActionException {
        super.perform();
        performImpl();
    }
    
    protected abstract void performImpl() throws IllegalActionException;

}
