package com.pipai.wf.artemis.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.artemis.components.EndpointsComponent;
import com.pipai.wf.artemis.components.InterpolationComponent;
import com.pipai.wf.artemis.components.PerspectiveCameraComponent;
import com.pipai.wf.artemis.components.PlayerUnitComponent;
import com.pipai.wf.artemis.components.SelectedUnitComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;

public class SelectedUnitSystem extends IteratingSystem {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectedUnitSystem.class);

	private ComponentMapper<SelectedUnitComponent> mSelectedUnit;
	private ComponentMapper<InterpolationComponent> mInterpolation;
	private ComponentMapper<XYZPositionComponent> mXyzPosition;
	private ComponentMapper<EndpointsComponent> mEndpoints;
	private ComponentMapper<PerspectiveCameraComponent> mPerspectiveCamera;

	private TagManager tagManager;

	private boolean processing;

	public SelectedUnitSystem() {
		super(Aspect.all(PlayerUnitComponent.class, SelectedUnitComponent.class, XYZPositionComponent.class));
		processing = true;
	}

	@Override
	protected boolean checkProcessing() {
		return processing;
	}

	@Override
	protected void inserted(int e) {
		LOGGER.debug("Unit was selected: " + e);
		Entity previous = tagManager.getEntity(Tag.SELECTED_UNIT.toString());
		if (previous != null) {
			mSelectedUnit.remove(previous);
		}
		tagManager.register(Tag.SELECTED_UNIT.toString(), world.getEntity(e));
		processing = true;
	}

	@Override
	protected void process(int entityId) {
		if (processing) {
			beginMovingCamera(mXyzPosition.get(entityId).position);
			processing = false;
		}
	}

	private void beginMovingCamera(Vector3 destination) {
		Entity camera = tagManager.getEntity(Tag.CAMERA.toString());
		InterpolationComponent cInterpolation = mInterpolation.create(camera);
		PerspectiveCameraComponent cPerspectiveCamera = mPerspectiveCamera.get(camera);
		EndpointsComponent cEndpoints = mEndpoints.create(camera);
		cEndpoints.start = cPerspectiveCamera.camera.position.cpy();
		cEndpoints.end = destination.cpy();
		cEndpoints.end.y -= 300;
		cEndpoints.end.z = cPerspectiveCamera.camera.position.z;
		cInterpolation.interpolation = Interpolation.sineOut;
		cInterpolation.maxT = 20;
		LOGGER.debug("Camera is moving to " + cEndpoints.end);
	}

}
