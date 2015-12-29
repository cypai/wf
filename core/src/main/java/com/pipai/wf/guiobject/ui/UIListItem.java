package com.pipai.wf.guiobject.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.Gui;
import com.pipai.wf.guiobject.GuiObject;
import com.pipai.wf.guiobject.GuiRenderable;
import com.pipai.wf.guiobject.LeftClickable;
import com.pipai.wf.guiobject.XYPositioned;

public class UIListItem extends GuiObject implements XYPositioned, GuiRenderable, LeftClickable {

	private Vector2 position;
	private float width, height;
	private Color color;

	public UIListItem(Gui gui, float x, float y, float width, float height, Color color) {
		super(gui);
		position = new Vector2(x, y);
		this.width = width;
		this.height = height;
		this.color = color;
	}

	@Override
	public int renderPriority() {
		return 0;
	}

	@Override
	public void onLeftClick(int gameX, int gameY) {

	}

	@Override
	public void render(BatchHelper batch) {
		ShapeRenderer r = batch.getShapeRenderer();
		r.begin(ShapeType.Line);
		r.setColor(color);
		r.rect(position.x, position.y, width, -height);
		r.end();
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

}
