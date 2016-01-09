package com.pipai.wf.artemis.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.pipai.wf.artemis.components.PerspectiveCameraComponent;

public class CameraUpdateSystem extends IteratingSystem {

	private ComponentMapper<PerspectiveCameraComponent> mPerspectiveCamera;

	public CameraUpdateSystem() {
		super(Aspect.all(PerspectiveCameraComponent.class));
	}

	@Override
	protected void process(int entityId) {
		PerspectiveCamera camera = mPerspectiveCamera.get(entityId).camera;
		camera.update();
	}

}
