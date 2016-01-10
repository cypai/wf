package com.pipai.wf.artemis.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.ComponentMapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.pipai.wf.artemis.components.CircleDecalComponent;
import com.pipai.wf.artemis.components.CircularShadowComponent;
import com.pipai.wf.artemis.components.PerspectiveCameraComponent;
import com.pipai.wf.artemis.components.PlayerUnitComponent;
import com.pipai.wf.artemis.components.SelectedUnitComponent;
import com.pipai.wf.artemis.components.VisibleComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;
import com.pipai.wf.battle.Battle;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.gui.BatchHelper;

/**
 * This system creates all initial entities for the Battle Screen
 */
public class BattleEntityCreationSystem extends ProcessOnceSystem {

	private static final Logger LOGGER = LoggerFactory.getLogger(BattleEntityCreationSystem.class);

	private ComponentMapper<XYZPositionComponent> mXyzPosition;
	private ComponentMapper<CircleDecalComponent> mCircle;
	private ComponentMapper<VisibleComponent> mVisible;
	private ComponentMapper<CircularShadowComponent> mCircularShadow;
	private ComponentMapper<PlayerUnitComponent> mPlayerUnit;
	private ComponentMapper<SelectedUnitComponent> mSelectedUnit;
	private ComponentMapper<PerspectiveCameraComponent> mPerspectiveCamera;

	private TagManager tagManager;
	private GroupManager groupManager;

	private final BatchHelper batch;
	private final Battle battle;

	public BattleEntityCreationSystem(BatchHelper batch, Battle battle) {
		this.batch = batch;
		this.battle = battle;
	}

	@Override
	protected void initialize() {
		generateCameras();
		generateUnits();
	}

	private void generateCameras() {
		int perspectiveCameraId = world.create();
		PerspectiveCamera camera = mPerspectiveCamera.create(perspectiveCameraId).camera;
		camera.position.set(0, -400, 300);
		camera.lookAt(0, 0, 0);
		camera.near = 1f;
		camera.far = 2000;
		XYZPositionComponent xyz = mXyzPosition.create(perspectiveCameraId);
		xyz.position = camera.position;
		tagManager.register(Tag.CAMERA.toString(), perspectiveCameraId);
		batch.set3DCamera(camera);
	}

	private void generateUnits() {
		boolean selectedUnitCreated = false;
		for (Agent agent : battle.getBattleMap().getAgents()) {
			int id = world.create();
			mVisible.create(id);
			mCircle.create(id);
			XYZPositionComponent xyz = mXyzPosition.create(id);
			xyz.position.set(agent.getPosition().getX() * 40, agent.getPosition().getY() * 40, 20);
			mCircularShadow.create(id);
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

	@Override
	protected void processOnce() {
		// Does nothing
	}

}
