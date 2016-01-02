package com.pipai.wf.guiobject.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.Gui;
import com.pipai.wf.guiobject.GuiObject;
import com.pipai.wf.guiobject.GuiRenderable;
import com.pipai.wf.guiobject.LeftClickable;

public class AttackButtonOverlay extends GuiObject implements GuiRenderable, LeftClickable {

	private static final int SQUARE_SIZE = 30;

	private int x, y;

	public AttackButtonOverlay(Gui gui) {
		super(gui);
	}

	@Override
	public int renderPriority() {
		return 0;
	}

	@Override
	public void onLeftClick(int gameX, int gameY) {

	}

	private void update(int screenW, int screenH) {
		x = screenW / 2;
		y = 30;
	}

	@Override
	public void render(BatchHelper batch) {
		update(getGui().getScreenWidth(), getGui().getScreenHeight());
		ShapeRenderer r = batch.getShapeRenderer();
		r.begin(ShapeType.Filled);
		r.setColor(new Color(0, 0.5f, 0.8f, 1));
		r.rect(x - SQUARE_SIZE / 2, y - SQUARE_SIZE / 2, SQUARE_SIZE, SQUARE_SIZE);
		r.end();
		SpriteBatch spr = batch.getSpriteBatch();
		BitmapFont font = batch.getFont();
		spr.begin();
		font.setColor(Color.WHITE);
		font.draw(spr, "A", x - SQUARE_SIZE / 4, y + SQUARE_SIZE / 4);
		spr.end();
	}

}
