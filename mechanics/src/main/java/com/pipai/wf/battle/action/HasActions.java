package com.pipai.wf.battle.action;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;

public interface HasActions {

	@JsonIgnore
	List<Action> getAvailableActions(BattleController controller, Agent agent);

	/**
	 * Gets an action of the actionClass. Returns null if not found.
	 */
	@JsonIgnore
	default Action getParticularAction(Class<? extends Action> actionClass, BattleController controller, Agent agent) {
		for (Action action : getAvailableActions(controller, agent)) {
			if (actionClass.isInstance(action)) {
				return action;
			}
		}
		return null;
	}

}
