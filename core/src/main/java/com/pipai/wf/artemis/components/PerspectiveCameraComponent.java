package com.pipai.wf.artemis.components;

import com.artemis.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;

public class PerspectiveCameraComponent extends Component {

	public final PerspectiveCamera camera;

	public PerspectiveCameraComponent() {
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

}
