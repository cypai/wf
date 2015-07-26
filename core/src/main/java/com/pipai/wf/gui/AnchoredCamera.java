package com.pipai.wf.gui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class AnchoredCamera {
	
	private Camera camera;
	private Vector3 anchor;
	
	private float cameraMoveTime;
	private Vector3 cameraStart, cameraDest, anchorStart, anchorDest;
	
	private float STEP_SIZE = 0.05f;
	
	private int angle;
	
	private final Vector3 rot_axis = new Vector3(0, 0, 1);
	
	public AnchoredCamera(int screenWidth, int screenHeight) {
        camera = new PerspectiveCamera(67, screenWidth, screenHeight);
        camera.position.set(0, -200, 300);
        camera.lookAt(0, 0, 0);
        camera.near = 1f;
        camera.far = 2000;
        anchor = new Vector3(0, 0, 0);
        angle = 90;
	}
	
	public void setViewport(float width, float height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
	}
	
	public void moveUp() {
		cameraMoveTime = 2;
		Vector3 v = new Vector3(0, 3, 0);
		v.rotate(rot_axis, angle - 90);
		camera.translate(v);
		anchor.add(v);
	}
	
	public void moveDown() {
		cameraMoveTime = 2;
		Vector3 v = new Vector3(0, -3, 0);
		v.rotate(rot_axis, angle - 90);
		camera.translate(v);
		anchor.add(v);
	}
	
	public void moveLeft() {
		cameraMoveTime = 2;
		Vector3 v = new Vector3(-3, 0, 0);
		v.rotate(rot_axis, angle - 90);
		camera.translate(v);
		anchor.add(v);
	}
	
	public void moveRight() {
		cameraMoveTime = 2;
		Vector3 v = new Vector3(3, 0, 0);
		v.rotate(rot_axis, angle - 90);
		camera.translate(v);
		anchor.add(v);
	}
	
	public void moveTo(float x, float y) {
		float xdiff = camera.position.x - anchor.x;
		float ydiff = camera.position.y - anchor.y;
		cameraMoveTime = 0;
		cameraStart = camera.position.cpy();
		cameraDest = new Vector3(x + xdiff, y + ydiff, camera.position.z);
		anchorStart = anchor.cpy();
		anchorDest = new Vector3(x, y, anchor.z);
	}
	
	public void arcballRotationCW() {
		camera.rotateAround(anchor, rot_axis, -45);
		angle -= 45;
		if (angle < 0) {
			angle += 360;
		}
	}
	
	public void arcballRotationCCW() {
		camera.rotateAround(anchor, rot_axis, 45);
		angle += 45;
		if (angle > 360) {
			angle -= 360;
		}
	}
	
	public void increaseHeight() {
		camera.position.z += 100;
		anchor.z += 100;
	}
	
	public void decreaseHeight() {
		camera.position.z -= 100;
		anchor.z -= 100;
	}
	
	public Matrix4 getProjectionMatrix() {
		return camera.combined;
	}
	
	public void update() {
		if (cameraMoveTime < 1.0 + 0.0001) {
			Interpolation interp = Interpolation.sineOut;
			camera.position.set(cameraStart.cpy().interpolate(cameraDest, cameraMoveTime, interp));
			anchor.set(anchorStart.cpy().interpolate(anchorDest, cameraMoveTime, interp));
			cameraMoveTime += STEP_SIZE;
		}
		camera.update();
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public Vector3 position() {
		return anchor.cpy();
	}
	
}
