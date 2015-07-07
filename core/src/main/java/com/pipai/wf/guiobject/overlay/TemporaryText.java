package com.pipai.wf.guiobject.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.BattleGUI;
import com.pipai.wf.guiobject.AnchoredGUIObject;

public class TemporaryText extends AnchoredGUIObject {

	public final boolean foreground = true;
	
	private float w, h;
	private int destroyAlarm;
	private String text;
	
	public TemporaryText(BattleGUI gui, Vector3 anchorPoint, float w, float h, String text) {
		super(gui, gui.getCamera(), anchorPoint);
		this.w = w;
		this.h = h;
		this.text = text;
		destroyAlarm = 120;
	}
	
	public int renderPriority() { return -1; }
	
	@Override
	public void update() {
		super.update();
		destroyAlarm--;
		if (destroyAlarm <= 0) {
			gui.deleteInstance(this);
		}
	}

	public void render(BatchHelper batch) {
		ShapeRenderer r = batch.getShapeRenderer();
		r.begin(ShapeType.Filled);
		r.setColor(new Color(0, 0.2f, 0.5f, 1));
		r.rect(screenPosition.x, screenPosition.y, w, h);
		r.end();
		SpriteBatch spr = batch.getSpriteBatch();
		BitmapFont font = batch.getFont();
		float line_height = font.getLineHeight();
		spr.begin();
		font.setColor(Color.WHITE);
		font.draw(spr, text, screenPosition.x + 6, screenPosition.y + h - (h - line_height)/2);
		spr.end();
	}
	
}
