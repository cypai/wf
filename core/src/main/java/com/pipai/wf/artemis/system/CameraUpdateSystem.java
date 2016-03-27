package com.pipai.wf.artemis.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.artemis.components.OrthographicCameraComponent;
import com.pipai.wf.artemis.components.PerspectiveCameraComponent;
import com.pipai.wf.artemis.components.SphericalCoordinateComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;
import com.pipai.wf.artemis.system.input.HeldKeys;

public class CameraUpdateSystem extends IteratingSystem implements InputProcessor {

	// private static final Logger LOGGER = LoggerFactory.getLogger(SelectedUnitSystem.class);

	private ComponentMapper<PerspectiveCameraComponent> mPerspectiveCamera;
	private ComponentMapper<OrthographicCameraComponent> mOrthoCamera;
	private ComponentMapper<XYZPositionComponent> mXyzPosition;
	private ComponentMapper<SphericalCoordinateComponent> mSphericalCoordinate;

	// private NeedsUpdateSystem needsUpdateSystem;

	private TagManager tagManager;

	private HeldKeys heldKeys;

	public CameraUpdateSystem() {
		super(Aspect.all(PerspectiveCameraComponent.class, XYZPositionComponent.class,
				SphericalCoordinateComponent.class));
		heldKeys = new HeldKeys();
	}

	public PerspectiveCamera getCamera() {
		return mPerspectiveCamera.get(tagManager.getEntity(Tag.CAMERA.toString())).camera;
	}

	public OrthographicCamera getOrthoCamera() {
		return mOrthoCamera.get(tagManager.getEntity(Tag.ORTHO_CAMERA.toString())).camera;
	}

	@Override
	protected void process(int entityId) {
		PerspectiveCamera camera = mPerspectiveCamera.get(entityId).camera;
		XYZPositionComponent cXyz = mXyzPosition.get(entityId);
		SphericalCoordinateComponent cSphericalCoordinates = mSphericalCoordinate.get(entityId);
		Vector3 pos = toCartesian(cSphericalCoordinates);
		Vector3 translation = getTranslationVector(cSphericalCoordinates);
		cXyz.position.add(translation);
		camera.position.set(cXyz.position.cpy().add(pos));
		camera.up.set(Vector3.Z);
		camera.lookAt(cXyz.position);
		camera.update();
	}

	private static Vector3 toCartesian(SphericalCoordinateComponent sphericalCoordinates) {
		return new Vector3().setFromSpherical(sphericalCoordinates.theta, sphericalCoordinates.phi)
				.scl(sphericalCoordinates.r);
	}

	private Vector3 getTranslationVector(SphericalCoordinateComponent sphericalCoordinates) {
		Vector3 translation = Vector3.Zero.cpy();
		float rad = (float) (sphericalCoordinates.theta + Math.PI / 2);
		if (heldKeys.isDown(Keys.W)) {
			translation.add(Vector3.Y.cpy().rotateRad(Vector3.Z, rad));
		}
		if (heldKeys.isDown(Keys.A)) {
			translation.add(Vector3.X.cpy().scl(-1).rotateRad(Vector3.Z, rad));
		}
		if (heldKeys.isDown(Keys.S)) {
			translation.add(Vector3.Y.cpy().scl(-1).rotateRad(Vector3.Z, rad));
		}
		if (heldKeys.isDown(Keys.D)) {
			translation.add(Vector3.X.cpy().rotateRad(Vector3.Z, rad));
		}
		return translation.scl(4);
	}

	@Override
	public boolean keyDown(int keycode) {
		boolean processed = true;
		Entity e = tagManager.getEntity(Tag.CAMERA.toString());
		SphericalCoordinateComponent cSphericalCoordinates = mSphericalCoordinate.get(e);
		heldKeys.keyDown(keycode);
		switch (keycode) {
		case Keys.Q:
			cSphericalCoordinates.theta -= Math.PI / 4;
			break;
		case Keys.E:
			cSphericalCoordinates.theta += Math.PI / 4;
			break;
		default:
			processed = false;
			break;
		}
		return processed;
	}

	@Override
	public boolean keyUp(int keycode) {
		heldKeys.keyUp(keycode);
		return true;
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
		Entity e = tagManager.getEntity(Tag.CAMERA.toString());
		SphericalCoordinateComponent cSphericalCoordinates = mSphericalCoordinate.get(e);
		cSphericalCoordinates.r += amount * 50;
		return true;
	}

}
