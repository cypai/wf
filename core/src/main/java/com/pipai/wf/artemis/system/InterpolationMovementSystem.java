package com.pipai.wf.artemis.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.pipai.wf.artemis.components.EndpointsComponent;
import com.pipai.wf.artemis.components.InterpolationComponent;
import com.pipai.wf.artemis.components.PerspectiveCameraComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;

public class InterpolationMovementSystem extends IteratingSystem {

	// private static final Logger LOGGER = LoggerFactory.getLogger(InterpolationMovementSystem.class);

	private ComponentMapper<XYZPositionComponent> mXyz;
	private ComponentMapper<PerspectiveCameraComponent> mPerspectiveCamera;
	private ComponentMapper<EndpointsComponent> mEndpoints;
	private ComponentMapper<InterpolationComponent> mInterpolation;

	@SuppressWarnings("unchecked")
	public InterpolationMovementSystem() {
		super(Aspect.all(EndpointsComponent.class, InterpolationComponent.class)
				.one(XYZPositionComponent.class, PerspectiveCameraComponent.class));
	}

	@Override
	protected void process(int entityId) {
		InterpolationComponent cInterpolation = mInterpolation.get(entityId);
		EndpointsComponent cEndpoints = mEndpoints.get(entityId);
		XYZPositionComponent xyz = mXyz.getSafe(entityId);
		if (xyz == null) {
			PerspectiveCamera camera = mPerspectiveCamera.get(entityId).camera;
			camera.position.set(cEndpoints.start.cpy().interpolate(cEndpoints.end, alpha(cInterpolation), cInterpolation.interpolation));
		} else {
			xyz.position.set(cEndpoints.start.cpy().interpolate(cEndpoints.end, alpha(cInterpolation), cInterpolation.interpolation));
			// LOGGER.debug("Entity " + entityId + " is at " + xyz.position);
		}
	}

	private static float alpha(InterpolationComponent interpolation) {
		return ((float) interpolation.t) / ((float) interpolation.maxT);
	}

}
