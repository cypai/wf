package com.pipai.wf.artemis.system.init;

import com.artemis.ComponentMapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.artemis.components.AgentComponent;
import com.pipai.wf.artemis.components.CircleDecalComponent;
import com.pipai.wf.artemis.components.CircularShadowComponent;
import com.pipai.wf.artemis.components.OrthographicCameraComponent;
import com.pipai.wf.artemis.components.PerspectiveCameraComponent;
import com.pipai.wf.artemis.components.PlayerUnitComponent;
import com.pipai.wf.artemis.components.SelectedUnitComponent;
import com.pipai.wf.artemis.components.SphericalCoordinateComponent;
import com.pipai.wf.artemis.components.SphericalRayPickInteractableComponent;
import com.pipai.wf.artemis.components.VisibleComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;
import com.pipai.wf.artemis.system.Group;
import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.artemis.system.Tag;
import com.pipai.wf.battle.Battle;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.gui.BatchHelper;

/**
 * This system creates all initial entities for the Battle Screen
 */
public class BattleEntityCreationSystem extends NoProcessingSystem {

	// private static final Logger LOGGER = LoggerFactory.getLogger(BattleEntityCreationSystem.class);

	private ComponentMapper<XYZPositionComponent> mXyzPosition;
	// private ComponentMapper<VelocityComponent> mVelocity;
	private ComponentMapper<CircleDecalComponent> mCircle;
	private ComponentMapper<VisibleComponent> mVisible;
	private ComponentMapper<CircularShadowComponent> mCircularShadow;
	private ComponentMapper<PlayerUnitComponent> mPlayerUnit;
	private ComponentMapper<SelectedUnitComponent> mSelectedUnit;
	private ComponentMapper<AgentComponent> mAgent;
	private ComponentMapper<PerspectiveCameraComponent> mPerspectiveCamera;
	private ComponentMapper<OrthographicCameraComponent> mOrthoCamera;
	private ComponentMapper<SphericalCoordinateComponent> mSphericalCoordinates;
	private ComponentMapper<SphericalRayPickInteractableComponent> mSphericalInteractable;

	private TagManager tagManager;
	private GroupManager groupManager;

	// private NeedsUpdateSystem needsUpdateSystem;

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
		Vector3 lookingAt = new Vector3();
		PerspectiveCamera camera = mPerspectiveCamera.create(perspectiveCameraId).camera;
		camera.position.set(0, -400, 300);
		camera.lookAt(lookingAt);
		camera.near = 1f;
		camera.far = 2000;
		XYZPositionComponent xyz = mXyzPosition.create(perspectiveCameraId);
		xyz.position = lookingAt.cpy();
		SphericalCoordinateComponent cSphericalCoordinates = mSphericalCoordinates.create(perspectiveCameraId);
		cSphericalCoordinates.r = lookingAt.dst(camera.position);
		cSphericalCoordinates.theta = (float) Math.atan(camera.position.y / camera.position.x);
		cSphericalCoordinates.phi = (float) Math.acos(camera.position.z / cSphericalCoordinates.r);
		// needsUpdateSystem.notify(perspectiveCameraId);
		tagManager.register(Tag.CAMERA.toString(), perspectiveCameraId);
		batch.set3DCamera(camera);

		int orthoCameraId = world.create();
		mOrthoCamera.create(orthoCameraId);
		tagManager.register(Tag.ORTHO_CAMERA.toString(), orthoCameraId);
	}

	private void generateUnits() {
		boolean selectedUnitCreated = false;
		for (Agent agent : battle.getBattleMap().getAgents()) {
			int id = world.create();
			mVisible.create(id);
			mCircle.create(id);
			XYZPositionComponent xyz = mXyzPosition.create(id);
			xyz.position.set(agent.getPosition().getX() * 40 + 20, agent.getPosition().getY() * 40 + 20, 20);
			mCircularShadow.create(id);
			AgentComponent cAgent = mAgent.create(id);
			cAgent.agent = agent;
			if (agent.getTeam().equals(Team.PLAYER)) {
				mPlayerUnit.create(id);
				mSphericalInteractable.create(id).radius = 16;
				groupManager.add(world.getEntity(id), Group.PLAYER_PARTY.toString());
				if (!selectedUnitCreated) {
					mSelectedUnit.create(id);
					selectedUnitCreated = true;
				}
			} else {
				groupManager.add(world.getEntity(id), Group.ENEMY_PARTY.toString());
			}
			// LOGGER.debug("Created entity at " + xyz.position);
		}
	}

}
