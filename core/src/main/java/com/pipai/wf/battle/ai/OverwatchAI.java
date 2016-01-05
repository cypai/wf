package com.pipai.wf.battle.ai;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.action.WaitAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Weapon;
import com.pipai.wf.item.weapon.WeaponFlag;

public class OverwatchAI extends AI {

	private static final Logger LOGGER = LoggerFactory.getLogger(OverwatchAI.class);

	private LinkedList<Agent> enemyAgents, toAct;

	public OverwatchAI(BattleController battleController) {
		super(battleController);
		enemyAgents = new LinkedList<Agent>();
		for (Agent a : getBattleMap().getAgents()) {
			if (a.getTeam() == Team.ENEMY) {
				enemyAgents.add(a);
			}
		}
	}

	@Override
	protected void startTurnInit() {
		toAct = new LinkedList<Agent>();
		for (Agent a : enemyAgents) {
			if (!a.isKO()) {
				toAct.add(a);
			}
		}
	}

	@Override
	public void performMove() {
		if (toAct.isEmpty()) {
			endTurn();
			return;
		}
		Agent a = toAct.poll();
		if (a.getAP() > 0) {
			Weapon w = AiHelper.getAgentWeapon(a);

			try {
				if (w.hasFlag(WeaponFlag.OVERWATCH)) {
					OverwatchAction ow = (OverwatchAction) w.getParticularAction(OverwatchAction.class, getBattleController(), a);
					ow.perform();
				} else {
					new WaitAction(getBattleController(), a).perform();
				}
			} catch (IllegalActionException e) {
				LOGGER.error("AI tried to perform illegal move: " + e.getMessage());
			}
		}
	}

}
