package com.pipai.wf.artemis.screen;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.pipai.wf.WFGame;
import com.pipai.wf.artemis.system.PartialTextBoxUpdateSystem;
import com.pipai.wf.artemis.system.PartialTextListAdvancementSystem;
import com.pipai.wf.artemis.system.init.VisualNovelEntityCreationSystem;
import com.pipai.wf.artemis.system.input.ExitInputProcessor;
import com.pipai.wf.artemis.system.rendering.BatchRenderingSystem;
import com.pipai.wf.artemis.system.rendering.PartialTextBoxRenderingSystem;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.scenario.VisualNovelScene;

import net.mostlyoriginal.api.event.common.EventSystem;

public class VisualNovelScreen implements Screen {

	private Game game;
	private World world;
	private BatchHelper batch;

	private InputMultiplexer multiplexer;

	public VisualNovelScreen(WFGame game, VisualNovelScene vnScene) {
		batch = new BatchHelper(game.getSpriteBatch(), game.getShapeRenderer(), game.getModelBatch(), game.getFont());

		this.game = game;
		WorldConfiguration config = new WorldConfigurationBuilder()
				.with(
						// Managers
						new TagManager(),
						new GroupManager(),
						new EventSystem(),

						new PartialTextBoxUpdateSystem(),
						new PartialTextListAdvancementSystem(),
						new VisualNovelEntityCreationSystem(batch, vnScene))
				.withPassive(-1,
						// Rendering
						new PartialTextBoxRenderingSystem(),
						new BatchRenderingSystem(batch))
				.build();
		world = new World(config);
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new ExitInputProcessor());
		multiplexer.addProcessor(world.getSystem(PartialTextListAdvancementSystem.class));
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
