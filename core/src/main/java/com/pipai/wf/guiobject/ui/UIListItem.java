package com.pipai.wf.guiobject.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.Gui;
import com.pipai.wf.guiobject.GuiObject;
import com.pipai.wf.guiobject.LeftClickable;
import com.pipai.wf.guiobject.GuiRenderable;

public class UIListItem extends GuiObject implements GuiRenderable, LeftClickable {
	
	protected float x, y, width, height;
	protected Color color;
	
	public UIListItem(Gui gui, float x, float y, float width, float height, Color color) {
		super(gui);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	@Override
	public int renderPriority() { return 0; }

	@Override
	public void onLeftClick(int screenX, int screenY, int gameX, int gameY) {
		
	}

	@Override
	public void render(BatchHelper batch) {
		ShapeRenderer r = batch.getShapeRenderer();
		r.begin(ShapeType.Line);
		r.setColor(color);
		r.rect(x, y, width, -height);
		r.end();
	}
	
}
