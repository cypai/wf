package com.pipai.wf.battle.action;

import java.util.List;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.component.ActionInterface;
import com.pipai.wf.battle.action.component.PerformerComponent;
import com.pipai.wf.battle.action.verification.ActionVerifier;
import com.pipai.wf.battle.damage.DamageCalculator;
import com.pipai.wf.battle.damage.DamageDealer;
import com.pipai.wf.battle.damage.TargetedActionCalculator;
import com.pipai.wf.battle.event.BattleEvent;
import com.pipai.wf.battle.event.BattleLog;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.misc.HasDescription;
import com.pipai.wf.misc.HasName;

public abstract class Action implements HasName, HasDescription, ActionInterface {

	private BattleController battleController;
	private BattleLog log;
	private BattleMap battleMap;
	private DamageDealer damageDealer;

	public Action() {
		// Nothing, for lazy evaluation
	}

	public Action(BattleController controller) {
		battleController = controller;
		if (controller != null) {
			// TODO: Fix this hack for NullPointerException calling this constructor during OverwatchHelper.getName()
			log = controller.getLog();
			battleMap = controller.getBattleMap();
			damageDealer = new DamageDealer(battleMap);
		}
	}

	public final BattleController getBattleController() {
		return battleController;
	}

	public final void setBattleController(BattleController controller) {
		battleController = controller;
	}

	@Override
	public final BattleMap getBattleMap() {
		return battleMap;
	}

	public final BattleConfiguration getBattleConfiguration() {
		return battleController.getBattleConfiguration();
	}

	@Override
	public final TargetedActionCalculator getTargetedActionCalculator() {
		return getBattleConfiguration().getTargetedActionCalculator();
	}

	public final DamageCalculator getDamageCalculator() {
		return getBattleConfiguration().getDamageCalculator();
	}

	public final DamageDealer getDamageDealer() {
		return damageDealer;
	}

	protected abstract List<ActionVerifier> getVerifiers();

	public ActionVerificationResult verify() {
		for (ActionVerifier verifier : getVerifiers()) {
			ActionVerificationResult result = verifier.verify(this);
			if (!result.isValid()) {
				return result;
			}
		}
		return ActionVerificationResult.validResult();
	}

	public final void perform() throws IllegalActionException {
		ActionVerificationResult verification = verify();
		if (!verification.isValid()) {
			throw new IllegalActionException(verification.getReason());
		}
		performImpl();
		battleController.performPostActionNotifications();
		if (this instanceof PerformerComponent) {
			((PerformerComponent) this).getPerformer().onAction(this);
		}
		postPerform();
	}

	protected abstract void performImpl() throws IllegalActionException;

	/**
	 * Override if anything needs to be done after the action is complete
	 */
	protected void postPerform() {
		// Nothing
	}

	protected void logBattleEvent(BattleEvent ev) {
		if (log != null) {
			log.logEvent(ev);
		}
	}

}
