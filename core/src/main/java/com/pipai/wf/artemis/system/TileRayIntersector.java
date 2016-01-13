package com.pipai.wf.artemis.system;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.pipai.wf.battle.map.GridPosition;

public class TileRayIntersector {

	public static GridPosition getIntersectedTile(Ray ray) {
		Plane plane = new Plane(Vector3.Z, Vector3.Zero);
		Vector3 intersection = new Vector3();
		Intersector.intersectRayPlane(ray, plane, intersection);
		intersection.x /= 40;
		intersection.y /= 40;
		return new GridPosition((int) intersection.x, (int) intersection.y);
	}

}
