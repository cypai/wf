package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.component.TargetedComponentAggregate;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.event.BattleEvent;
import com.pipai.wf.battle.event.OverwatchActivationEvent;
import com.pipai.wf.exception.IllegalActionException;

public abstract class OverwatchableTargetedAction extends TargetedAction implements TargetedComponentAggregate {

	private boolean fromOverwatch;
	private BattleEvent overwatchEventParent;

	public OverwatchableTargetedAction(BattleController controller, Agent performerAgent, Agent targetAgent) {
		super(controller, performerAgent, targetAgent);
	}

	public OverwatchableTargetedAction(BattleController controller, Agent performerAgent) {
		super(controller, performerAgent);
	}

	public OverwatchableTargetedAction() {
		// Call super
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		fromOverwatch = false;
		performImpl(0);
	}

	protected abstract void performImpl(int owPenalty) throws IllegalActionException;

	public final void performOnOverwatch(BattleEvent parent, Agent target) throws IllegalActionException {
		fromOverwatch = true;
		overwatchEventParent = parent;
		setTarget(target);
		performImpl(1);
	}

	@Override
	protected void logBattleEvent(BattleEvent ev) {
		if (fromOverwatch) {
			overwatchEventParent.addChainEvent(
					new OverwatchActivationEvent(getPerformer(), getTarget(), getTarget().getPosition(), ev));
		} else {
			super.logBattleEvent(ev);
		}
	}

}
