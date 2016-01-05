package com.pipai.wf.battle.action;

import java.util.Arrays;
import java.util.List;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.component.DefaultApRequiredComponent;
import com.pipai.wf.battle.action.component.HasPerformerComponent;
import com.pipai.wf.battle.action.component.HasWeaponComponent;
import com.pipai.wf.battle.action.component.WeaponComponent;
import com.pipai.wf.battle.action.component.WeaponComponentImpl;
import com.pipai.wf.battle.action.verification.ActionVerifier;
import com.pipai.wf.battle.action.verification.BaseVerifier;
import com.pipai.wf.battle.action.verification.HasItemVerifier;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Weapon;
import com.pipai.wf.unit.ability.QuickReloadAbility;

public class ReloadAction extends PerformerAction implements DefaultApRequiredComponent, HasPerformerComponent, HasWeaponComponent {

	private WeaponComponent weaponComponent = new WeaponComponentImpl();

	public ReloadAction(BattleController controller, Agent performerAgent, Weapon weapon) {
		super(controller, performerAgent);
		setWeapon(weapon);
	}

	public ReloadAction(BattleController controller, Agent performerAgent) {
		super(controller, performerAgent);
	}

	public ReloadAction() {
		// Call super
	}

	@Override
	public WeaponComponent getWeaponComponent() {
		return weaponComponent;
	}

	@Override
	protected List<ActionVerifier> getVerifiers() {
		return Arrays.asList(
				BaseVerifier.getInstance(),
				new HasItemVerifier(getPerformer(), getWeapon()));
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		Agent agent = getPerformer();
		Weapon weapon = getWeapon();
		weapon.reload();
		if (agent.getAbilities().hasAbility(QuickReloadAbility.class)) {
			agent.setAP(agent.getAP() - 1);
		} else {
			agent.setAP(0);
		}
		logBattleEvent(BattleEvent.reloadEvent(agent));
	}

	@Override
	public String getName() {
		return "Reload";
	}

	@Override
	public String getDescription() {
		return "Reload your current weapon";
	}

}
