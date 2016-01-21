package com.pipai.wf.artemis.system;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import com.pipai.wf.artemis.components.AgentComponent;
import com.pipai.wf.battle.agent.Agent;

/**
 * Acts as a "singleton" for getting battle-related objects
 */
public class AgentEntitySystem extends NoProcessingSystem {

	// private static final Logger LOGGER = LoggerFactory.getLogger(AgentEntitySystem.class);

	private ComponentMapper<AgentComponent> mAgent;

	private GroupManager groupManager;

	public int getAgentEntity(Agent agent) {
		ImmutableBag<Entity> party = groupManager.getEntities(Group.PLAYER_PARTY.toString());
		for (Entity entity : party) {
			if (mAgent.get(entity).agent.equals(agent)) {
				return entity.getId();
			}
		}
		ImmutableBag<Entity> enemies = groupManager.getEntities(Group.ENEMY_PARTY.toString());
		for (Entity entity : enemies) {
			if (mAgent.get(entity).agent.equals(agent)) {
				return entity.getId();
			}
		}
		throw new IllegalArgumentException("No Agent was found matching the argument");
	}

}
