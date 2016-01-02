package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.component.ApRequiredComponent;
import com.pipai.wf.battle.action.component.HasPerformerComponent;
import com.pipai.wf.battle.action.component.PerformerComponent;
import com.pipai.wf.battle.action.component.PerformerComponentImpl;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.unit.ability.QuickReloadAbility;

public class ReloadAction extends Action implements ApRequiredComponent, HasPerformerComponent {

	private PerformerComponent performerComponent = new PerformerComponentImpl();

	public ReloadAction(BattleController controller, Agent performerAgent) {
		super(controller);
		setPerformer(performerAgent);
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

	@Override
	public PerformerComponent getPerformerComponent() {
		return performerComponent;
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		Agent agent = getPerformer();
		agent.getCurrentWeapon().reload();
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
