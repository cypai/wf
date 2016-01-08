package com.pipai.wf.artemis.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;

public class XYZPositionComponent extends Component {

	public Vector3 position;

	public XYZPositionComponent(float x, float y, float z) {
		position = new Vector3(x, y, z);
	}

	public XYZPositionComponent() {
		this(0, 0, 0);
	}

}
