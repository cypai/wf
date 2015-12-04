package com.pipai.wf.guiobject;

import com.badlogic.gdx.math.Vector3;

public interface XYZPositioned extends XYPositioned {

	float getZ();

	void setZ(float z);

	Vector3 getPosition();

}
