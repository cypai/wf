package com.pipai.wf.battle.action;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;

public interface HasActions {

	@JsonIgnore
	List<Action> getAvailableActions(BattleController controller, Agent agent);

	/**
	 * Gets an action of the actionClass.
	 */
	@SuppressWarnings("unchecked")
	@JsonIgnore
	default <T extends Action> Optional<T> getParticularAction(Class<T> actionClass, BattleController controller,
			Agent agent) {

		for (Action action : getAvailableActions(controller, agent)) {
			if (actionClass.isInstance(action)) {
				return Optional.of((T) action);
			}
		}
		return Optional.empty();
	}

}
