package com.pipai.wf.artemis.system.battle;

import java.util.ArrayList;
import java.util.List;

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
import com.pipai.wf.artemis.components.XYZPositionComponent;
import com.pipai.wf.artemis.system.Tag;

public class CameraInterpolationMovementSystem extends IteratingSystem {

	private ComponentMapper<InterpolationComponent> mInterpolation;
	private ComponentMapper<XYZPositionComponent> mXyzPosition;
	private ComponentMapper<EndpointsComponent> mEndpoints;

	private TagManager tagManager;

	private List<Runnable> callbacks;

	public CameraInterpolationMovementSystem() {
		super(Aspect.all(PerspectiveCameraComponent.class, XYZPositionComponent.class,
				InterpolationComponent.class));
		callbacks = new ArrayList<>();
	}

	public void addOneTimeCallbackOnFinish(Runnable callback) {
		callbacks.add(callback);
	}

	/**
	 * Begin moving the camera to the destination on next process iteration
	 *
	 * @param destination
	 */
	public void beginMovingCamera(Vector3 destination) {
		Entity camera = tagManager.getEntity(Tag.CAMERA.toString());
		XYZPositionComponent cXyz = mXyzPosition.get(camera);
		InterpolationComponent cInterpolation = mInterpolation.create(camera);
		EndpointsComponent cEndpoints = mEndpoints.create(camera);
		cEndpoints.start = cXyz.position.cpy();
		cEndpoints.end = destination.cpy();
		cEndpoints.end.z = cXyz.position.z;
		cInterpolation.interpolation = Interpolation.sineOut;
		// Reset t if interpolation pre-exists
		cInterpolation.t = 0;
		cInterpolation.maxT = 20;
	}

	@Override
	protected void removed(int entityId) {
		for (Runnable callback : callbacks) {
			callback.run();
		}
		callbacks.clear();
	}

	@Override
	protected boolean checkProcessing() {
		return false;
	}

	@Override
	protected void process(int entityId) {
	}

}
