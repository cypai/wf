package com.pipai.wf.artemis.system;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.pipai.wf.battle.map.GridPosition;

public final class TileGridPositionUtils {

	public static final int TILE_SIZE = 40;

	private TileGridPositionUtils() {
	}

	public static Vector3 gridPositionToTileCenter(GridPosition position) {
		return new Vector3(position.getX() * TILE_SIZE + TILE_SIZE / 2, position.getY() * TILE_SIZE + TILE_SIZE / 2, 0);
	}

	public static GridPosition toGridPosition(Vector3 position) {
		return new GridPosition((int) position.x / TILE_SIZE, (int) position.y / TILE_SIZE);
	}

	public static GridPosition getIntersectedTile(Ray ray) {
		Plane plane = new Plane(Vector3.Z, Vector3.Zero);
		Vector3 intersection = new Vector3();
		Intersector.intersectRayPlane(ray, plane, intersection);
		intersection.x /= TILE_SIZE;
		intersection.y /= TILE_SIZE;
		return new GridPosition((int) intersection.x, (int) intersection.y);
	}

}
