package com.pipai.wf.artemis.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;

public class VelocityComponent extends Component {

	public Vector3 velocity;

	public VelocityComponent() {
		velocity = Vector3.Zero.cpy();
	}

}
