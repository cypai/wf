package com.pipai.wf.artemis.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.ComponentMapper;
import com.artemis.managers.GroupManager;
import com.pipai.wf.artemis.components.CircleDecalComponent;
import com.pipai.wf.artemis.components.PlayerUnitComponent;
import com.pipai.wf.artemis.components.SelectedUnitComponent;
import com.pipai.wf.artemis.components.VisibleComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;
import com.pipai.wf.battle.Battle;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;

public class UnitEntityCreationSystem extends ProcessOnceSystem {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnitEntityCreationSystem.class);

	private ComponentMapper<XYZPositionComponent> mXyzPosition;
	private ComponentMapper<CircleDecalComponent> mCircle;
	private ComponentMapper<VisibleComponent> mVisible;
	private ComponentMapper<PlayerUnitComponent> mPlayerUnit;
	private ComponentMapper<SelectedUnitComponent> mSelectedUnit;

	private GroupManager groupManager;

	private final Battle battle;

	public UnitEntityCreationSystem(Battle battle) {
		this.battle = battle;
	}

	@Override
	protected void processOnce() {
		boolean selectedUnitCreated = false;
		for (Agent agent : battle.getBattleMap().getAgents()) {
			int id = world.create();
			mVisible.create(id);
			mCircle.create(id);
			XYZPositionComponent xyz = mXyzPosition.create(id);
			xyz.position.set(agent.getPosition().getX() * 40, agent.getPosition().getY() * 40, 0);
			if (agent.getTeam().equals(Team.PLAYER)) {
				mPlayerUnit.create(id);
				groupManager.add(world.getEntity(id), Group.PLAYER_PARTY.toString());
				if (!selectedUnitCreated) {
					mSelectedUnit.create(id);
					selectedUnitCreated = true;
				}
			} else {
				groupManager.add(world.getEntity(id), Group.ENEMY_PARTY.toString());
			}
			LOGGER.debug("Created entity at " + xyz.position);
		}
	}

}
