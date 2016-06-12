package com.pipai.wf.battle.action;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

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

	@Override
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
		BattleEvent ev = performImpl();
		logBattleEvent(ev);
		battleController.performPostActionNotifications();
		if (this instanceof PerformerComponent) {
			((PerformerComponent) this).getPerformer().onAction(this);
		}
		postPerform();
	}

	/**
	 * The actual implementation of the action.
	 * 
	 * @return A BattleEvent containing the details of the implementation for logging
	 * @throws IllegalActionException
	 */
	protected abstract BattleEvent performImpl() throws IllegalActionException;

	/**
	 * Override if anything needs to be done after the action is complete
	 */
	protected void postPerform() {
		// Nothing
	}

	protected void logBattleEvent(BattleEvent ev) {
		Objects.requireNonNull(ev, "BattleEvent cannot be null, check actionEvent() in " + getClass());
		if (log != null) {
			log.logEvent(ev);
		}
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, "battleController", "battleMap", "log", "damageDealer");
	}

}
