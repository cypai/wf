package com.pipai.wf.artemis.system.battleanimation;

import java.util.LinkedList;

import com.artemis.ComponentMapper;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.math.Interpolation;
import com.pipai.wf.artemis.components.AgentComponent;
import com.pipai.wf.artemis.components.EndpointsComponent;
import com.pipai.wf.artemis.components.InterpolationComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;
import com.pipai.wf.artemis.event.AnimationEndEvent;
import com.pipai.wf.artemis.event.InterpolationEndEvent;
import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.artemis.system.Tag;
import com.pipai.wf.artemis.system.TileGridPositionUtils;
import com.pipai.wf.artemis.system.battle.AgentEntitySystem;
import com.pipai.wf.artemis.system.battle.SelectedUnitSystem;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.event.MoveEvent;
import com.pipai.wf.util.GridPosition;

import net.mostlyoriginal.api.event.common.EventSystem;
import net.mostlyoriginal.api.event.common.Subscribe;

public class MoveEventAnimationHandler extends NoProcessingSystem {

	// private static final Logger LOGGER = LoggerFactory.getLogger(MoveEventAnimationHandler.class);

	private ComponentMapper<XYZPositionComponent> mXyz;
	private ComponentMapper<EndpointsComponent> mEndpoints;
	private ComponentMapper<InterpolationComponent> mInterpolation;
	private ComponentMapper<AgentComponent> mAgent;

	private EventSystem eventSystem;
	private TagManager tagManager;
	private AgentEntitySystem agentEntitySystem;
	private SelectedUnitSystem selectedUnitSystem;

	private LinkedList<GridPosition> path;

	private int interpolationEndEventKey;

	@Subscribe
	public void handleMoveEvent(MoveEvent event) {
		path = event.path;
		moveAgent(agentEntitySystem.getAgentEntity(event.performer), path.pollFirst(), path.peekFirst());
	}

	@Subscribe
	public void handleInterpolationEndEvent(InterpolationEndEvent event) {
		if (event.getKey() == interpolationEndEventKey) {
			if (path.size() > 1) {
				moveAgent(event.entityId, path.pollFirst(), path.peekFirst());
			} else {
				selectedUnitSystem.updateForSelectedAgent();
				eventSystem.dispatch(new AnimationEndEvent());
			}
		}
	}

	private void moveAgent(int id, GridPosition start, GridPosition end) {
		float z = mXyz.get(id).position.z;
		EndpointsComponent cEndpoints = mEndpoints.create(id);
		cEndpoints.start = TileGridPositionUtils.gridPositionToTileCenter(start);
		cEndpoints.start.z = z;
		cEndpoints.end = TileGridPositionUtils.gridPositionToTileCenter(end);
		cEndpoints.end.z = z;
		InterpolationComponent cInterpolation = mInterpolation.create(id);
		cInterpolation.interpolation = Interpolation.linear;
		// t set to 1 to prevent choppiness between movements (otherwise it spends 2 frames at the same position)
		cInterpolation.t = 1;
		cInterpolation.maxT = 6;

		cInterpolation.onEndEvent = new InterpolationEndEvent();
		interpolationEndEventKey = cInterpolation.onEndEvent.getKey();

		if (mAgent.get(id).agent.getTeam().equals(Team.ENEMY)) {
			int cameraId = tagManager.getEntity(Tag.CAMERA.toString()).getId();
			float cameraZ = mXyz.get(cameraId).position.z;
			EndpointsComponent cCameraEndpoints = mEndpoints.create(cameraId);
			cCameraEndpoints.start = TileGridPositionUtils.gridPositionToTileCenter(start);
			cCameraEndpoints.start.z = cameraZ;
			cCameraEndpoints.end = TileGridPositionUtils.gridPositionToTileCenter(end);
			cCameraEndpoints.end.z = cameraZ;
			InterpolationComponent cCameraInterpolation = mInterpolation.create(cameraId);
			cCameraInterpolation.interpolation = Interpolation.linear;
			// t set to 1 to prevent choppiness between movements (otherwise it spends 2 frames at the same position)
			cCameraInterpolation.t = 1;
			cCameraInterpolation.maxT = 6;
		}
	}
}
