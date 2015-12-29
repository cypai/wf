package com.pipai.wf.guiobject;

import com.badlogic.gdx.math.Vector3;

public interface XYZPositioned {

	Vector3 getPosition();

	default float getX() {
		return getPosition().x;
	}

	default float getY() {
		return getPosition().y;
	}

	default float getZ() {
		return getPosition().z;
	}

	default void setX(float x) {
		getPosition().x = x;
	}

	default void setY(float y) {
		getPosition().y = y;
	}

	default void setZ(float z) {
		getPosition().z = z;
	}

}
