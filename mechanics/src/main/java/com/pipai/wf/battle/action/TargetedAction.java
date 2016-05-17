package com.pipai.wf.battle.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.component.HasPerformerTargetComponents;
import com.pipai.wf.battle.action.component.PerformerTargetComponentImpl;
import com.pipai.wf.battle.agent.Agent;

public abstract class TargetedAction extends Action implements HasPerformerTargetComponents {

	private static final Logger LOGGER = LoggerFactory.getLogger(TargetedAction.class);

	private PerformerTargetComponentImpl performerTargetComponentImpl = new PerformerTargetComponentImpl();

	public TargetedAction(BattleController controller, Agent performerAgent, Agent targetAgent) {
		super(controller);
		setPerformer(performerAgent);
		setTarget(targetAgent);
	}

	public TargetedAction(BattleController controller, Agent performerAgent) {
		super(controller);
		setPerformer(performerAgent);
	}

	public TargetedAction() {
		// Call super
	}

	@Override
	public PerformerTargetComponentImpl getPerformerTargetComponentImpl() {
		return performerTargetComponentImpl;
	}

	@Override
	protected void postPerform() {
		Agent performer = getPerformer();
		Agent target = getTarget();
		if (performer.getTeam() != target.getTeam() && target.isKO()) {
			int exp = target.getExpGiven();
			LOGGER.debug(target.getName() + " was KOed by " + performer.getName() + ": +" + exp + " EXP");
			performer.giveExp(exp);
		}
	}

}
