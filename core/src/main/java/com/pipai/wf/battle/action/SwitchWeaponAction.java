package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.component.HasPerformerComponent;
import com.pipai.wf.battle.action.component.PerformerComponent;
import com.pipai.wf.battle.action.component.PerformerComponentImpl;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.exception.IllegalActionException;

public class SwitchWeaponAction extends Action implements HasPerformerComponent {

	private PerformerComponent performerComponent = new PerformerComponentImpl();

	public SwitchWeaponAction(BattleController controller, Agent performerAgent) {
		super(controller);
		setPerformer(performerAgent);
	}

	@Override
	public PerformerComponent getPerformerComponent() {
		return performerComponent;
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		getPerformer().switchWeapon();
		logBattleEvent(BattleEvent.switchWeaponEvent(getPerformer()));
	}

	@Override
	public String getName() {
		return "Switch Weapon";
	}

	@Override
	public String getDescription() {
		return "Switch to a different weapon";
	}

}
