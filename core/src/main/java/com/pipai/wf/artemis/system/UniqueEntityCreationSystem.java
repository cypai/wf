package com.pipai.wf.artemis.system;

import com.artemis.ComponentMapper;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.pipai.wf.artemis.components.PerspectiveCameraComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;

public class UniqueEntityCreationSystem extends ProcessOnceSystem {

	private ComponentMapper<PerspectiveCameraComponent> mPerspectiveCamera;
	private ComponentMapper<XYZPositionComponent> mXyzPosition;

	private TagManager tagManager;

	@Override
	protected void processOnce() {
		int perspectiveCameraId = world.create();
		PerspectiveCamera camera = mPerspectiveCamera.create(perspectiveCameraId).camera;
		camera.position.set(0, -300, 400);
		camera.lookAt(0, 0, 0);
		camera.near = 1f;
		camera.far = 2000;
		XYZPositionComponent xyz = mXyzPosition.create(perspectiveCameraId);
		xyz.position = camera.position;
		tagManager.register(Tag.CAMERA.toString(), perspectiveCameraId);
	}

}
