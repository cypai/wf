package com.pipai.wf;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.pipai.wf.gui.MainMenuGui;

public class WFGame extends Game {

	private WfConfiguration wfConfig;

	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	private ModelBatch modelBatch;
	private BitmapFont font;

	@Override
	public void create() {
		wfConfig = new WfConfiguration();
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		modelBatch = new ModelBatch();
		font = new BitmapFont();
		setScreen(new MainMenuGui(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		shapeRenderer.dispose();
		modelBatch.dispose();
		font.dispose();
	}

	public WfConfiguration getWfConfiguration() {
		return wfConfig;
	}

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	public ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}

	public ModelBatch getModelBatch() {
		return modelBatch;
	}

	public BitmapFont getFont() {
		return font;
	}

}
