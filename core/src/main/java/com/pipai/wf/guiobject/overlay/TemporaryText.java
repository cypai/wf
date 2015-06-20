package com.pipai.wf.guiobject.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.GUI;
import com.pipai.wf.guiobject.GUIObject;
import com.pipai.wf.guiobject.Renderable;

public class TemporaryText extends GUIObject implements Renderable {

	public final boolean foreground = true;
	
	private float x, y, w, h;
	private int destroyAlarm;
	private String text;
	
	public TemporaryText(GUI gui, float x, float y, float w, float h, String text) {
		super(gui);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.text = text;
		destroyAlarm = 120;
	}
	
	public int renderPriority() { return -1; }
	
	@Override
	public void update() {
		destroyAlarm--;
		if (destroyAlarm <= 0) {
			gui.deleteInstance(this);
		}
	}

	public void render(BatchHelper batch) {
		ShapeRenderer r = batch.getShapeRenderer();
		r.begin(ShapeType.Filled);
		r.setColor(new Color(0, 0.2f, 0.5f, 1));
		r.rect(x, y, w, h);
		r.end();
		SpriteBatch spr = batch.getSpriteBatch();
		BitmapFont font = batch.getFont();
		float line_height = font.getLineHeight();
		spr.begin();
		font.setColor(Color.WHITE);
		font.draw(spr, text, x + 6, y + h - (h - line_height)/2);
		spr.end();
	}
	
}
