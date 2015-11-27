package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.DamageCalculator;
import com.pipai.wf.battle.damage.DamageDealer;
import com.pipai.wf.battle.damage.TargetedActionCalculator;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.log.BattleLog;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.misc.HasDescription;
import com.pipai.wf.misc.HasName;

public abstract class Action implements HasName, HasDescription {

	private BattleController controller;
	private BattleLog log;
	private Agent performerAgent;
	private BattleMap map;
	private DamageDealer damageDealer;

	public Action(BattleController controller, Agent performerAgent) {
		this.performerAgent = performerAgent;
		this.controller = controller;
		if (controller != null) {
			// TODO: Fix this hack for NullPointerException calling this constructor during OverwatchHelper.getName()
			log = controller.getLog();
			map = controller.getBattleMap();
			damageDealer = new DamageDealer(map);
		}
	}

	public final Agent getPerformer() {
		return performerAgent;
	}

	public final BattleController getBattleController() {
		return controller;
	}

	public final BattleMap getBattleMap() {
		return map;
	}

	public final BattleConfiguration getBattleConfiguration() {
		return controller.getBattleConfiguration();
	}

	public final TargetedActionCalculator getTargetedActionCalculator() {
		return getBattleConfiguration().getTargetedActionCalculator();
	}

	public final DamageCalculator getDamageCalculator() {
		return getBattleConfiguration().getDamageCalculator();
	}

	public final DamageDealer getDamageDealer() {
		return damageDealer;
	}

	public final void perform() throws IllegalActionException {
		if (performerAgent.getAP() < getAPRequired()) {
			throw new IllegalActionException("Not enough AP for action");
		}
		if (performerAgent.isKO()) {
			throw new IllegalActionException("KOed unit cannot act");
		}
		performImpl();
		controller.performPostActionNotifications();
		performerAgent.onAction(this);
	}

	protected abstract void performImpl() throws IllegalActionException;

	/*
	 * Returns the minimum AP required to perform the action
	 */
	public abstract int getAPRequired();

	protected final void log(BattleEvent ev) {
		if (log != null) {
			log.logEvent(ev);
		}
	}

}
