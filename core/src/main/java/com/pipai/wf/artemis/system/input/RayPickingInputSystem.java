package com.pipai.wf.artemis.system.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.pipai.wf.artemis.components.SphericalRayPickInteractableComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;
import com.pipai.wf.artemis.event.LeftClickEvent;
import com.pipai.wf.artemis.system.CameraUpdateSystem;

import net.mostlyoriginal.api.event.common.EventSystem;

public class RayPickingInputSystem extends BaseSystem implements InputProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(RayPickingInputSystem.class);

	private ComponentMapper<XYZPositionComponent> mXyz;
	private ComponentMapper<SphericalRayPickInteractableComponent> mSphericalInteractable;

	private EventSystem eventSystem;

	private CameraUpdateSystem cameraUpdateSystem;

	@Override
	protected boolean checkProcessing() {
		return false;
	}

	@Override
	protected void processSystem() {
		// Do nothing
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Ray ray = cameraUpdateSystem.getCamera().getPickRay(screenX, screenY);
		IntBag entities = world.getAspectSubscriptionManager()
				.get(Aspect.all(XYZPositionComponent.class, SphericalRayPickInteractableComponent.class)).getEntities();
		int result = -1;
		float distance = -1;
		for (int i = 0; i < entities.size(); i++) {
			int id = entities.get(i);
			Vector3 position = mXyz.get(id).position;
			float dist2 = ray.origin.dst2(position);
			if (distance >= 0f && dist2 > distance) {
				continue;
			}
			if (Intersector.intersectRaySphere(ray, position, mSphericalInteractable.get(id).radius, null)) {
				result = id;
				distance = dist2;
			}
		}
		LOGGER.debug("Result: " + result);
		if (result >= 0) {
			eventSystem.dispatch(new LeftClickEvent(result));
		}
		return result >= 0;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
