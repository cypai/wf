package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.battle.weapon.WeaponFlag;
import com.pipai.wf.exception.IllegalActionException;

public class SuppressionAction extends TargetedAction {

	public SuppressionAction(BattleController controller, Agent performerAgent, Agent targetAgent) {
		super(controller, performerAgent, targetAgent);
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		Agent a = getPerformer();
		Weapon w = a.getCurrentWeapon();
		if (!w.hasFlag(WeaponFlag.SUPPRESSION)) {
			throw new IllegalActionException(w.getName() + " cannot use suppression");
		}
		if (w.currentAmmo() < 2) {
			throw new IllegalActionException("Not enough ammo to suppress");
		}
		Agent target = getTarget();
		a.suppressOther(target);
		a.setAP(0);
		a.getCurrentWeapon().expendAmmo(2);
		logBattleEvent(BattleEvent.targetedActionEvent(a, target, this));
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

	@Override
	public String getName() {
		return "Suppression";
	}

	@Override
	public String getDescription() {
		return "Decreases the target's aim, crit chance, and AOE range";
	}

}
