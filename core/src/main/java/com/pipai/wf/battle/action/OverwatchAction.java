package com.pipai.wf.battle.action;

import java.util.Arrays;
import java.util.List;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.component.DefaultApRequiredComponent;
import com.pipai.wf.battle.action.component.HasWeaponComponent;
import com.pipai.wf.battle.action.component.WeaponComponent;
import com.pipai.wf.battle.action.component.WeaponComponentImpl;
import com.pipai.wf.battle.action.verification.ActionVerifier;
import com.pipai.wf.battle.action.verification.BaseVerifier;
import com.pipai.wf.battle.action.verification.HasItemVerifier;
import com.pipai.wf.battle.action.verification.WeaponAmmoVerifier;
import com.pipai.wf.battle.action.verification.WeaponFlagVerifier;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.WeaponFlag;

public class OverwatchAction extends PerformerAction implements DefaultApRequiredComponent, HasWeaponComponent {

	private WeaponComponent weaponComponent = new WeaponComponentImpl();
	private OverwatchableTargetedAction overwatchAction;

	public OverwatchAction(BattleController controller, Agent performerAgent) {
		super(controller, performerAgent);
	}

	public OverwatchAction(BattleController controller) {
		super(controller);
	}

	public OverwatchAction() {
		// Call super
	}

	@Override
	public WeaponComponent getWeaponComponent() {
		return weaponComponent;
	}

	public void setOverwatchAction(OverwatchableTargetedAction action) {
		overwatchAction = action;
	}

	@Override
	protected List<ActionVerifier> getVerifiers() {
		return Arrays.asList(
				BaseVerifier.getInstance(),
				new HasItemVerifier(getPerformer(), getWeapon()),
				new WeaponAmmoVerifier(getWeapon(), 1),
				new WeaponFlagVerifier(getWeapon(), WeaponFlag.OVERWATCH, "This weapon cannot overwatch"));
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		Agent performer = getPerformer();
		// TODO: Check this, performer needs to specify weapon overwatch
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
