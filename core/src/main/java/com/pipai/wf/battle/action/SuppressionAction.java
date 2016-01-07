package com.pipai.wf.battle.action;

import java.util.Arrays;
import java.util.List;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.component.ApRequiredComponent;
import com.pipai.wf.battle.action.component.HasWeaponComponent;
import com.pipai.wf.battle.action.component.WeaponComponent;
import com.pipai.wf.battle.action.component.WeaponComponentImpl;
import com.pipai.wf.battle.action.verification.ActionVerifier;
import com.pipai.wf.battle.action.verification.BaseVerifier;
import com.pipai.wf.battle.action.verification.HasAbilityVerifier;
import com.pipai.wf.battle.action.verification.HasItemVerifier;
import com.pipai.wf.battle.action.verification.WeaponAmmoVerifier;
import com.pipai.wf.battle.action.verification.WeaponFlagVerifier;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Weapon;
import com.pipai.wf.item.weapon.WeaponFlag;
import com.pipai.wf.unit.ability.SuppressionAbility;

public class SuppressionAction extends TargetedAction implements ApRequiredComponent, HasWeaponComponent {

	private WeaponComponent weaponComponent = new WeaponComponentImpl();

	public SuppressionAction(BattleController controller, Agent performerAgent, Agent targetAgent) {
		super(controller, performerAgent, targetAgent);
	}

	public SuppressionAction(BattleController controller, Agent performerAgent, Weapon weapon) {
		super(controller, performerAgent);
		setWeapon(weapon);
	}

	public SuppressionAction(BattleController controller, Agent performerAgent, Agent targetAgent, Weapon weapon) {
		super(controller, performerAgent, targetAgent);
		setWeapon(weapon);
	}

	@Override
	public WeaponComponent getWeaponComponent() {
		return weaponComponent;
	}

	@Override
	protected List<ActionVerifier> getVerifiers() {
		return Arrays.asList(
				BaseVerifier.getInstance(),
				new HasAbilityVerifier(getPerformer(), SuppressionAbility.class),
				new HasItemVerifier(getPerformer(), getWeapon()),
				new WeaponFlagVerifier(getWeapon(), WeaponFlag.SUPPRESSION, "This weapon cannot use suppression"),
				new WeaponAmmoVerifier(getWeapon(), 2));
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		Agent a = getPerformer();
		Weapon w = getWeapon();
		Agent target = getTarget();
		a.suppressOther(target);
		a.setAP(0);
		w.expendAmmo(2);
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
