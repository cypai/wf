package com.pipai.wf.artemis.components;

import com.artemis.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class OrthographicCameraComponent extends Component {

	public final OrthographicCamera camera;

	public OrthographicCameraComponent() {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

}
