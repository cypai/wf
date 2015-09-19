package com.pipai.wf.guiobject.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.BattleGui;
import com.pipai.wf.guiobject.AnchoredGuiObject;

public class TemporaryText extends AnchoredGuiObject {

	public final boolean foreground = true;

	private int destroyAlarm;
	private String text;
	private GlyphLayout glayout;

	public TemporaryText(BattleGui gui, Vector3 anchorPoint, String text) {
		super(gui, gui.getRayMapper(), anchorPoint);
		glayout = new GlyphLayout();
		this.text = text;
		destroyAlarm = 120;
	}

	@Override
	public int renderPriority() {
		return -1;
	}

	@Override
	public void update() {
		super.update();
		destroyAlarm--;
		if (destroyAlarm <= 0) {
			gui.deleteInstance(this);
		}
	}

	@Override
	public void render(BatchHelper batch) {
		ShapeRenderer r = batch.getShapeRenderer();
		SpriteBatch spr = batch.getSpriteBatch();
		BitmapFont font = batch.getFont();
		glayout.setText(font, text);
		r.begin(ShapeType.Filled);
		r.setColor(new Color(0, 0.2f, 0.5f, 1));
		r.rect(screenPosition.x, screenPosition.y, glayout.width + 12, glayout.height + 12);
		r.end();
		spr.begin();
		font.setColor(Color.WHITE);
		font.draw(spr, text, screenPosition.x + 6, screenPosition.y + font.getLineHeight());
		spr.end();
	}

}
