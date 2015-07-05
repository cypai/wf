package com.pipai.wf.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public class RayMapper {
	
	private Camera camera;
	
	public RayMapper(Camera camera) {
		this.camera = camera;
	}
	
	public Ray screenToRay(Vector2 screenPos) {
		return screenToRay(screenPos.x, screenPos.y);
	}
	
	public Ray screenToRay(float screenX, float screenY) {
		return camera.getPickRay(screenX, screenY);
	}
	
	public Vector2 pointToScreen(Vector3 point) {
		Vector3 projection = camera.project(point);
		return new Vector2(projection.x, projection.y);
	}
	
	public Vector2 pointToScreen(float x, float y, float z) {
		return pointToScreen(new Vector3(x, y, z));
	}
	
}
