package com.pipai.wf.artemis.system.battleanimation;

import java.util.LinkedList;

import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Interpolation;
import com.pipai.wf.artemis.components.EndpointsComponent;
import com.pipai.wf.artemis.components.InterpolationComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;
import com.pipai.wf.artemis.event.InterpolationEndEvent;
import com.pipai.wf.artemis.system.AgentEntitySystem;
import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.artemis.system.TileGridPositionUtils;
import com.pipai.wf.battle.event.MoveEvent;
import com.pipai.wf.util.GridPosition;

import net.mostlyoriginal.api.event.common.Subscribe;

public class MoveEventAnimationHandler extends NoProcessingSystem {

	// private static final Logger LOGGER = LoggerFactory.getLogger(MoveEventAnimationHandler.class);

	private ComponentMapper<XYZPositionComponent> mXyz;
	private ComponentMapper<EndpointsComponent> mEndpoints;
	private ComponentMapper<InterpolationComponent> mInterpolation;

	private AgentEntitySystem agentEntitySystem;

	private LinkedList<GridPosition> path;

	private int interpolationEndEventKey;

	@Subscribe
	public void handleMoveEvent(MoveEvent event) {
		path = event.path;
		moveAgent(agentEntitySystem.getAgentEntity(event.performer), path.pollFirst(), path.peekFirst());
	}

	@Subscribe
	public void handleInterpolationEndEvent(InterpolationEndEvent event) {
		if (event.getKey() == interpolationEndEventKey && path.size() > 1) {
			moveAgent(event.entityId, path.pollFirst(), path.peekFirst());
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
		cInterpolation.t = 0;
		cInterpolation.maxT = 6;
		cInterpolation.onEndEvent = new InterpolationEndEvent();
		interpolationEndEventKey = cInterpolation.onEndEvent.getKey();
	}

}
