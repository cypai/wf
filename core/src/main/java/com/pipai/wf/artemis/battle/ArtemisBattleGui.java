package com.pipai.wf.artemis.battle;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.pipai.wf.WFGame;
import com.pipai.wf.artemis.system.BatchRenderingSystem;
import com.pipai.wf.artemis.system.CircleRenderingSystem;
import com.pipai.wf.artemis.system.FpsRenderingSystem;
import com.pipai.wf.artemis.system.InputProcessingSystem;
import com.pipai.wf.artemis.system.UnitEntityCreationSystem;
import com.pipai.wf.battle.Battle;
import com.pipai.wf.gui.BatchHelper;

public class ArtemisBattleGui implements Screen {

	private Game game;
	private World world;
	private PerspectiveCamera camera;
	private BatchHelper batch;
	private BatchRenderingSystem renderingSystem;

	public ArtemisBattleGui(WFGame game, Battle battle) {
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0, -300, 400);
		camera.lookAt(0, 0, 0);
		camera.near = 1f;
		camera.far = 2000;

		batch = new BatchHelper(game.getSpriteBatch(), game.getShapeRenderer(), game.getModelBatch(), game.getFont());
		batch.set3DCamera(camera);

		this.game = game;
		WorldConfiguration config = new WorldConfigurationBuilder()
				.with(
						new FpsRenderingSystem(batch),
						new InputProcessingSystem(),
						new UnitEntityCreationSystem(battle),
						new CircleRenderingSystem(batch, camera))
				.build();
		renderingSystem = new BatchRenderingSystem(batch);
		world = new World(config);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		camera.update();
		world.setDelta(delta);
		world.process();
		renderingSystem.process();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		world.dispose();
	}

}
