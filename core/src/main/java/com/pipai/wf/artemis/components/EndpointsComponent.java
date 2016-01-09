package com.pipai.wf.artemis.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;

public class EndpointsComponent extends Component {

	public Vector3 start;
	public Vector3 end;

	public EndpointsComponent() {
		start = new Vector3();
		end = new Vector3();
	}

}
