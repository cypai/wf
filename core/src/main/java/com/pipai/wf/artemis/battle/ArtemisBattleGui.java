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
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.pipai.wf.WFGame;
import com.pipai.wf.artemis.system.BattleSystem;
import com.pipai.wf.artemis.system.CameraUpdateSystem;
import com.pipai.wf.artemis.system.InterpolationIncrementSystem;
import com.pipai.wf.artemis.system.InterpolationMovementSystem;
import com.pipai.wf.artemis.system.MovableTileHighlightSystem;
import com.pipai.wf.artemis.system.SelectedUnitSystem;
import com.pipai.wf.artemis.system.UiSystem;
import com.pipai.wf.artemis.system.VelocitySystem;
import com.pipai.wf.artemis.system.init.BattleEntityCreationSystem;
import com.pipai.wf.artemis.system.input.InputProcessingSystem;
import com.pipai.wf.artemis.system.input.RayPickingInputSystem;
import com.pipai.wf.artemis.system.rendering.BatchRenderingSystem;
import com.pipai.wf.artemis.system.rendering.CircleRenderingSystem;
import com.pipai.wf.artemis.system.rendering.CircularShadowRenderingSystem;
import com.pipai.wf.artemis.system.rendering.FpsRenderingSystem;
import com.pipai.wf.artemis.system.rendering.TerrainRenderingSystem;
import com.pipai.wf.artemis.system.rendering.TileHighlightRenderingSystem;
import com.pipai.wf.battle.Battle;
import com.pipai.wf.gui.BatchHelper;

import net.mostlyoriginal.api.event.common.EventSystem;
import net.mostlyoriginal.plugin.ProfilerPlugin;

public class ArtemisBattleGui implements Screen {

	private Game game;
	private World world;
	private BatchHelper batch;

	public ArtemisBattleGui(WFGame game, Battle battle) {
		batch = new BatchHelper(game.getSpriteBatch(), game.getShapeRenderer(), game.getModelBatch(), game.getFont());

		this.game = game;
		WorldConfiguration config = new WorldConfigurationBuilder()
				.dependsOn(ProfilerPlugin.class)
				// .withPassive(1, new NeedsUpdateSystem())
				.with(
						// Managers
						new TagManager(),
						new GroupManager(),
						new EventSystem(),

						// Entity Creation
						new BattleEntityCreationSystem(batch, battle),

						// Input
						new InputProcessingSystem(),
						new RayPickingInputSystem(),

						// Misc
						new CameraUpdateSystem(),
						new InterpolationMovementSystem(),
						new InterpolationIncrementSystem(),
						new VelocitySystem(),

						// Battle Related
						new BattleSystem(battle),
						new SelectedUnitSystem(),
						new MovableTileHighlightSystem())
				.withPassive(-1,
						// Rendering
						new TerrainRenderingSystem(batch, battle.getBattleMap()),
						new CircleRenderingSystem(),
						new CircularShadowRenderingSystem(),
						new TileHighlightRenderingSystem(),
						new FpsRenderingSystem(),
						new BatchRenderingSystem(batch))
				.withPassive(-2,
						// Rendering
						new UiSystem())
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
		PerspectiveCamera camera = world.getSystem(CameraUpdateSystem.class).getCamera();
		camera.viewportWidth = width;
		camera.viewportHeight = height;
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
