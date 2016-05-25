package com.pipai.wf.artemis.screen;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.pipai.wf.WFGame;
import com.pipai.wf.artemis.system.AgentEntitySystem;
import com.pipai.wf.artemis.system.BattleSystem;
import com.pipai.wf.artemis.system.CameraUpdateSystem;
import com.pipai.wf.artemis.system.InterpolationIncrementSystem;
import com.pipai.wf.artemis.system.InterpolationMovementSystem;
import com.pipai.wf.artemis.system.MouseHoverTileSystem;
import com.pipai.wf.artemis.system.MovableTileHighlightSystem;
import com.pipai.wf.artemis.system.SelectedUnitSystem;
import com.pipai.wf.artemis.system.TimedDestroySystem;
import com.pipai.wf.artemis.system.UiSystem;
import com.pipai.wf.artemis.system.VelocitySystem;
import com.pipai.wf.artemis.system.battleaction.MoveActionSystem;
import com.pipai.wf.artemis.system.battleanimation.MoveEventAnimationHandler;
import com.pipai.wf.artemis.system.battleanimation.OverwatchEventAnimationHandler;
import com.pipai.wf.artemis.system.battleanimation.ReloadEventAnimationHandler;
import com.pipai.wf.artemis.system.init.BattleEntityCreationSystem;
import com.pipai.wf.artemis.system.input.BattleKeysInputProcessorSystem;
import com.pipai.wf.artemis.system.input.InputProcessingSystem;
import com.pipai.wf.artemis.system.input.RayPickingInputSystem;
import com.pipai.wf.artemis.system.rendering.AnchoredTextRenderingSystem;
import com.pipai.wf.artemis.system.rendering.BatchRenderingSystem;
import com.pipai.wf.artemis.system.rendering.CircleRenderingSystem;
import com.pipai.wf.artemis.system.rendering.CircularShadowRenderingSystem;
import com.pipai.wf.artemis.system.rendering.FpsRenderingSystem;
import com.pipai.wf.artemis.system.rendering.MouseHoverTileRenderingSystem;
import com.pipai.wf.artemis.system.rendering.TerrainRenderingSystem;
import com.pipai.wf.artemis.system.rendering.TileHighlightRenderingSystem;
import com.pipai.wf.battle.Battle;
import com.pipai.wf.gui.BatchHelper;

import net.mostlyoriginal.api.event.common.EventSystem;
import net.mostlyoriginal.plugin.ProfilerPlugin;

// SUPPRESS CHECKSTYLE ClassComplexity This class is actually fairly simple
public class ArtemisBattleScreen extends SwitchableScreen {

	private World world;
	private BatchHelper batch;

	public ArtemisBattleScreen(WFGame game, Battle battle) {
		super(game);

		batch = new BatchHelper(game.getSpriteBatch(), game.getShapeRenderer(), game.getModelBatch(), game.getFont());

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
						new BattleKeysInputProcessorSystem(),

						// Misc
						new InterpolationMovementSystem(),
						new InterpolationIncrementSystem(),
						new VelocitySystem(),
						new TimedDestroySystem(),

						// Battle Related
						new BattleSystem(battle),
						new AgentEntitySystem(),
						new SelectedUnitSystem(),
						new MovableTileHighlightSystem(),
						new MouseHoverTileSystem(),
						new MoveActionSystem(),

						// Animation Handlers
						new ReloadEventAnimationHandler(),
						new OverwatchEventAnimationHandler(),
						new MoveEventAnimationHandler())
				.withPassive(-1,
						// Rendering
						new CameraUpdateSystem(),
						new TerrainRenderingSystem(batch, battle.getBattleMap()),
						new CircleRenderingSystem(),
						new CircularShadowRenderingSystem(),
						new TileHighlightRenderingSystem(),
						new MouseHoverTileRenderingSystem(),
						new FpsRenderingSystem(),
						new BatchRenderingSystem(batch))
				.withPassive(-2,
						// Rendering
						new AnchoredTextRenderingSystem())
				.withPassive(-3,
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
		camera.update();
		OrthographicCamera orthoCamera = world.getSystem(CameraUpdateSystem.class).getOrthoCamera();
		orthoCamera.setToOrtho(false, width, height);
		orthoCamera.update();
		world.getSystem(BatchRenderingSystem.class).getBatch().getSpriteBatch()
				.setProjectionMatrix(orthoCamera.combined);
		world.getSystem(BatchRenderingSystem.class).getBatch().getShapeRenderer()
				.setProjectionMatrix(orthoCamera.combined);
		world.getSystem(UiSystem.class).getStage().getViewport().setCamera(orthoCamera);
		world.getSystem(UiSystem.class).getStage().getViewport().update(width, height, false);
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
