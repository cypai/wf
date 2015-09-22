package com.pipai.wf.guiobject;

import com.badlogic.gdx.math.Vector3;

public interface XYZPositioned extends XYPositioned {

	public float getZ();

	public void setZ(float z);

	public Vector3 getPosition();

}
