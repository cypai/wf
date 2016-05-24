package com.pipai.wf.artemis.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.artemis.components.EndpointsComponent;
import com.pipai.wf.artemis.components.InterpolationComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;

public class InterpolationMovementSystem extends IteratingSystem {

	// private static final Logger LOGGER = LoggerFactory.getLogger(InterpolationMovementSystem.class);

	private ComponentMapper<XYZPositionComponent> mXyz;
	private ComponentMapper<EndpointsComponent> mEndpoints;
	private ComponentMapper<InterpolationComponent> mInterpolation;

	// private NeedsUpdateSystem needsUpdateSystem;

	public InterpolationMovementSystem() {
		super(Aspect.all(EndpointsComponent.class, InterpolationComponent.class, XYZPositionComponent.class));
	}

	@Override
	protected void process(int entityId) {
		InterpolationComponent cInterpolation = mInterpolation.get(entityId);
		EndpointsComponent cEndpoints = mEndpoints.get(entityId);
		XYZPositionComponent xyz = mXyz.get(entityId);
		Vector3 newPosition = cEndpoints.start.cpy().interpolate(cEndpoints.end, alpha(cInterpolation),
				cInterpolation.interpolation);
		xyz.position.set(newPosition);
		// needsUpdateSystem.notify(entityId);
		// LOGGER.debug("Entity " + entityId + " is at " + xyz.position);
	}

	private static float alpha(InterpolationComponent interpolation) {
		return ((float) interpolation.t) / ((float) interpolation.maxT);
	}

}
