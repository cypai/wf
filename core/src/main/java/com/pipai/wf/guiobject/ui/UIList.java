package com.pipai.wf.guiobject.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.Gui;
import com.pipai.wf.guiobject.GuiObject;
import com.pipai.wf.guiobject.GuiRenderable;

public class UIList extends GuiObject implements GuiRenderable {
	
	protected ArrayList<UIListItem> list;
	protected float x, y, width, height;
	protected Color color;
	
	public UIList(Gui gui, float x, float y, float width, float height, Color color) {
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
	public void render(BatchHelper batch) {
		ShapeRenderer r = batch.getShapeRenderer();
		r.begin(ShapeType.Line);
		r.setColor(color);
		r.rect(x, y, width, -height);
		r.end();
	}
	
	
}
