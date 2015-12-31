package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.battle.weapon.WeaponFlag;
import com.pipai.wf.exception.IllegalActionException;

public class OverwatchAction extends AlterStateAction {

	private OverwatchableTargetedAction overwatchAction;

	public OverwatchAction(BattleController controller, Agent performerAgent) {
		super(controller, performerAgent);
		overwatchAction = new WeaponActionFactory(controller).defaultWeaponActionSchema(performerAgent);
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		Agent performer = getPerformer();
		Weapon weapon = performer.getCurrentWeapon();
		if (weapon == null) {
			throw new IllegalActionException("Not currently wielding a weapon");
		}
		if (!weapon.hasFlag(WeaponFlag.OVERWATCH)) {
			throw new IllegalActionException(weapon.getName() + " cannot overwatch");
		}
		if (weapon.hasFlag(WeaponFlag.NEEDS_AMMUNITION)) {
			if (weapon.currentAmmo() == 0) {
				throw new IllegalActionException("Not enough ammo to overwatch");
			}
		}
		performer.setOverwatch(overwatchAction);
		logBattleEvent(BattleEvent.overwatchEvent(getPerformer(), overwatchAction.getName()));
	}

	@Override
	public String getName() {
		return "Overwatch";
	}

	@Override
	public String getDescription() {
		return "Attack the first enemy that moves in range";
	}

}
