package com.pipai.wf;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.pipai.wf.gui.MainMenuGui;

public class WFGame extends Game {

	public SpriteBatch sprBatch;
	public ShapeRenderer shapeBatch;
	public ModelBatch modelBatch;
	public BitmapFont font;

	@Override
	public void create() {
		sprBatch = new SpriteBatch();
		shapeBatch = new ShapeRenderer();
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
		sprBatch.dispose();
		shapeBatch.dispose();
		modelBatch.dispose();
		font.dispose();
	}

}
