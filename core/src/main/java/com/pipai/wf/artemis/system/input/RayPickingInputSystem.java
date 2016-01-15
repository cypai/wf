package com.pipai.wf.artemis.system.input;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.pipai.wf.artemis.components.SphericalRayPickInteractableComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;
import com.pipai.wf.artemis.event.LeftClickEvent;
import com.pipai.wf.artemis.event.MouseHoverRayEvent;
import com.pipai.wf.artemis.event.RightClickRayEvent;
import com.pipai.wf.artemis.system.CameraUpdateSystem;
import com.pipai.wf.artemis.system.NoProcessingSystem;

import net.mostlyoriginal.api.event.common.EventSystem;

public class RayPickingInputSystem extends NoProcessingSystem implements InputProcessor {

	// private static final Logger LOGGER = LoggerFactory.getLogger(RayPickingInputSystem.class);

	private ComponentMapper<XYZPositionComponent> mXyz;
	private ComponentMapper<SphericalRayPickInteractableComponent> mSphericalInteractable;

	private EventSystem eventSystem;

	private CameraUpdateSystem cameraUpdateSystem;

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
		switch (button) {
		case Buttons.LEFT:
			processLeftClick(screenX, screenY);
			break;
		case Buttons.RIGHT:
			processRightClick(screenX, screenY);
			break;
		default:
			return false;
		}
		return true;
	}

	private void processLeftClick(int screenX, int screenY) {
		Ray ray = cameraUpdateSystem.getCamera().getPickRay(screenX, screenY);
		int playerEntity = getRayIntersectingPlayerUnit(ray);
		// LOGGER.debug("Result: " + playerEntity);
		if (playerEntity >= 0) {
			eventSystem.dispatch(new LeftClickEvent(playerEntity));
		}
	}

	private int getRayIntersectingPlayerUnit(Ray ray) {
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
		return result;
	}

	private void processRightClick(int screenX, int screenY) {
		Ray ray = cameraUpdateSystem.getCamera().getPickRay(screenX, screenY);
		eventSystem.dispatch(new RightClickRayEvent(ray));
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
		Ray ray = cameraUpdateSystem.getCamera().getPickRay(screenX, screenY);
		eventSystem.dispatch(new MouseHoverRayEvent(ray));
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
