package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.component.TargetedComponentAggregate;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.event.BattleEvent;
import com.pipai.wf.battle.event.OverwatchActivationEvent;
import com.pipai.wf.exception.IllegalActionException;

public abstract class OverwatchableTargetedAction extends TargetedAction implements TargetedComponentAggregate {

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
	protected BattleEvent performImpl() throws IllegalActionException {
		return performImpl(0);
	}

	/**
	 * Same as {@link Action#performImpl()}, but with an overwatch penalty.
	 *
	 * @see Action#performImpl()
	 * @param owPenalty
	 *            amount of times the overwatch penalty is to be applied
	 */
	protected abstract BattleEvent performImpl(int owPenalty) throws IllegalActionException;

	/**
	 * Performs this action on Overwatch. Modifies the parent BattleEvent by adding the chain event to it.
	 *
	 * @param parent
	 *            the parent event, which is modified by adding a chain event of the overwatch event details
	 * @param target
	 * @throws IllegalActionException
	 */
	public final void performOnOverwatch(BattleEvent parent, Agent target) throws IllegalActionException {
		setTarget(target);
		BattleEvent currentEvent = performImpl(1);
		parent.addChainEvent(new OverwatchActivationEvent(getPerformer(), target, target.getPosition(), currentEvent));
	}

}
