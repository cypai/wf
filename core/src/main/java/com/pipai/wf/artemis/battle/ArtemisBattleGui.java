package com.pipai.wf.artemis.battle;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.pipai.wf.WFGame;
import com.pipai.wf.artemis.system.BatchRenderingSystem;
import com.pipai.wf.artemis.system.BattleEntityCreationSystem;
import com.pipai.wf.artemis.system.BattleSystem;
import com.pipai.wf.artemis.system.CameraUpdateSystem;
import com.pipai.wf.artemis.system.CircleRenderingSystem;
import com.pipai.wf.artemis.system.FpsRenderingSystem;
import com.pipai.wf.artemis.system.InputProcessingSystem;
import com.pipai.wf.artemis.system.InterpolationIncrementSystem;
import com.pipai.wf.artemis.system.InterpolationMovementSystem;
import com.pipai.wf.artemis.system.SelectedUnitSystem;
import com.pipai.wf.artemis.system.TerrainRenderingSystem;
import com.pipai.wf.battle.Battle;
import com.pipai.wf.gui.BatchHelper;

public class ArtemisBattleGui implements Screen {

	private Game game;
	private World world;
	private BatchHelper batch;

	public ArtemisBattleGui(WFGame game, Battle battle) {
		batch = new BatchHelper(game.getSpriteBatch(), game.getShapeRenderer(), game.getModelBatch(), game.getFont());

		this.game = game;
		WorldConfiguration config = new WorldConfigurationBuilder()
				.with(

						// Managers
						new TagManager(),
						new GroupManager(),

						// Entity Creation
						new BattleEntityCreationSystem(batch, battle),

						// Input
						new InputProcessingSystem(),

						// Misc
						new CameraUpdateSystem(),
						new InterpolationMovementSystem(),
						new InterpolationIncrementSystem(),

						// Battle Related
						new BattleSystem(battle),
						new SelectedUnitSystem())
				.withPassive(0,
						// Rendering
						new TerrainRenderingSystem(batch, battle.getBattleMap()),
						new CircleRenderingSystem(),
						new FpsRenderingSystem(batch),
						new BatchRenderingSystem(batch))
				.build();
		world = new World(config);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		world.setDelta(delta);
		world.process();
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
