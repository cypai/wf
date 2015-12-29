package com.pipai.wf.guiobject.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.Gui;
import com.pipai.wf.guiobject.GuiObject;
import com.pipai.wf.guiobject.GuiRenderable;
import com.pipai.wf.guiobject.LeftClickable;
import com.pipai.wf.guiobject.XYPositioned;
import com.pipai.wf.util.UtilFunctions;

public abstract class CenteredButton extends GuiObject implements XYPositioned, GuiRenderable, LeftClickable {

	private Vector2 position;
	private float w, h;
	private Color border, backgroundColor;
	private String label;

	private GlyphLayout glayout;

	public CenteredButton(Gui gui, float x, float y, float width, float height, Color border, Color background, String label) {
		super(gui);
		position = new Vector2(x, y);
		w = width;
		h = height;
		this.border = border;
		backgroundColor = background;
		this.label = label;
		glayout = new GlyphLayout();
	}

	@Override
	public final void onLeftClick(int gameX, int gameY) {
		if (UtilFunctions.isInBoundingBox(position.x, position.y, w, h, gameX, gameY)) {
			onLeftClickImpl();
		}
	}

	protected abstract void onLeftClickImpl();

	@Override
	public int renderPriority() {
		return 0;
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

	@Override
	public void render(BatchHelper batch) {
		ShapeRenderer r = batch.getShapeRenderer();
		r.begin(ShapeType.Filled);
		r.setColor(backgroundColor);
		r.rect(position.x - w / 2, position.y - h / 2, w, h);
		r.end();
		r.begin(ShapeType.Line);
		r.setColor(border);
		r.rect(position.x - w / 2, position.y - h / 2, w, h);
		r.end();
		SpriteBatch spr = batch.getSpriteBatch();
		BitmapFont font = batch.getFont();
		glayout.setText(font, label);
		spr.begin();
		font.setColor(Color.BLACK);
		font.draw(spr, label, position.x - glayout.width / 2, position.y + glayout.height / 2);
		spr.end();
	}

}
