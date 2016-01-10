package com.pipai.wf.artemis.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.artemis.components.NeedsUpdateComponent;
import com.pipai.wf.artemis.components.PerspectiveCameraComponent;
import com.pipai.wf.artemis.components.SphericalCoordinateComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;

public class CameraUpdateSystem extends IteratingSystem implements InputProcessor {

	// private static final Logger LOGGER = LoggerFactory.getLogger(SelectedUnitSystem.class);

	private ComponentMapper<PerspectiveCameraComponent> mPerspectiveCamera;
	private ComponentMapper<XYZPositionComponent> mXyzPosition;
	private ComponentMapper<SphericalCoordinateComponent> mSphericalCoordinate;

	private NeedsUpdateSystem needsUpdateSystem;

	private TagManager tagManager;

	public CameraUpdateSystem() {
		super(Aspect.all(PerspectiveCameraComponent.class, XYZPositionComponent.class, SphericalCoordinateComponent.class, NeedsUpdateComponent.class));
	}

	@Override
	protected void process(int entityId) {
		PerspectiveCamera camera = mPerspectiveCamera.get(entityId).camera;
		XYZPositionComponent cXyz = mXyzPosition.get(entityId);
		SphericalCoordinateComponent cSphericalCoordinates = mSphericalCoordinate.get(entityId);
		Vector3 pos = toCartesian(cSphericalCoordinates);
		camera.position.set(cXyz.position.cpy().add(pos));
		camera.up.set(0, 0, 1);
		camera.lookAt(cXyz.position);
		camera.update();
	}

	private static Vector3 toCartesian(SphericalCoordinateComponent sphericalCoordinates) {
		float x = (float) (sphericalCoordinates.r * Math.sin(sphericalCoordinates.phi) * Math.cos(sphericalCoordinates.theta));
		float y = (float) (sphericalCoordinates.r * Math.sin(sphericalCoordinates.phi) * Math.sin(sphericalCoordinates.theta));
		float z = (float) (sphericalCoordinates.r * Math.cos(sphericalCoordinates.phi));
		return new Vector3(x, y, z);
	}

	@Override
	public boolean keyDown(int keycode) {
		boolean processed = true;
		Entity e = tagManager.getEntity(Tag.CAMERA.toString());
		SphericalCoordinateComponent cSphericalCoordinates = mSphericalCoordinate.get(e);
		switch (keycode) {
		case Keys.Q:
			cSphericalCoordinates.theta -= Math.PI / 4;
			needsUpdateSystem.notify(e);
			break;
		case Keys.E:
			cSphericalCoordinates.theta += Math.PI / 4;
			needsUpdateSystem.notify(e);
			break;
		default:
			processed = false;
			break;
		}
		return processed;
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
		return false;
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
