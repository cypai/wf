package com.pipai.wf.guiobject;

import com.badlogic.gdx.math.Vector2;

public interface XYPositioned {

	Vector2 getPosition();

	default float getX() {
		return getPosition().x;
	}

	default float getY() {
		return getPosition().y;
	}

	default void setX(float x) {
		getPosition().x = x;
	}

	default void setY(float y) {
		getPosition().y = y;
	}

}
