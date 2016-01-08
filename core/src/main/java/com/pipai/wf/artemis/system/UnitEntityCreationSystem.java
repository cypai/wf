package com.pipai.wf.artemis.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.pipai.wf.artemis.components.CircleDecalComponent;
import com.pipai.wf.artemis.components.VisibleComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;
import com.pipai.wf.battle.Battle;
import com.pipai.wf.battle.agent.Agent;

public class UnitEntityCreationSystem extends BaseSystem {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnitEntityCreationSystem.class);

	ComponentMapper<XYZPositionComponent> xyzMapper;
	ComponentMapper<CircleDecalComponent> circleMapper;
	ComponentMapper<VisibleComponent> visibleMapper;

	private final Battle battle;

	private boolean processing;

	public UnitEntityCreationSystem(Battle battle) {
		this.battle = battle;
		processing = true;
	}

	@Override
	protected boolean checkProcessing() {
		return processing;
	}

	@Override
	protected void processSystem() {
		if (processing) {
			for (Agent agent : battle.getBattleMap().getAgents()) {
				int id = world.create();
				visibleMapper.create(id);
				circleMapper.create(id);
				XYZPositionComponent xyz = xyzMapper.create(id);
				xyz.position.set(agent.getPosition().getX() * 40, agent.getPosition().getY() * 40, 0);
				LOGGER.debug("Created entity at " + xyz.position);
			}
			// Only run once
			processing = false;
		}
	}

}
