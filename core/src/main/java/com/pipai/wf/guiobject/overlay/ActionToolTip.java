package com.pipai.wf.guiobject.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;
import com.pipai.wf.battle.attack.Attack;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.BattleGUI;
import com.pipai.wf.guiobject.GUIObject;
import com.pipai.wf.guiobject.Renderable;

public class ActionToolTip extends GUIObject implements Renderable {
	
	protected BattleGUI gui;
	private final int PADDING = 6;
	private float x, y, width, height;
	private String title, description;
	private int accuracy, critProb;
	
	public ActionToolTip(BattleGUI gui, float x, float y, float width, float height) {
		super(gui);
		this.gui = gui;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public int renderPriority() { return 0;	}
	
	public void setToAttackDescription(Attack atk, int accuracy, int critProb) {
		title = "Attack";
		description = atk.description();
		this.accuracy = accuracy;
		this.critProb = critProb;
	}

	@Override
	public void render(BatchHelper batch) {
		if (gui.getMode() != BattleGUI.Mode.TARGET_SELECT) {
			return;
		}
		ShapeRenderer r = batch.getShapeRenderer();
		SpriteBatch spr = batch.getSpriteBatch();
		BitmapFont f = batch.getFont();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		r.begin(ShapeType.Filled);
		r.setColor(new Color(0.5f, 0.5f, 0.5f, 0.7f));
		r.rect(x, y, width, -height);
		r.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		spr.begin();
		f.setColor(Color.WHITE);
		f.draw(spr, title, (x + width)/2, y - f.getLineHeight(), 0, Align.center, true);
		f.draw(spr, description, (x + width)/2, y - 2.5f * f.getLineHeight(), 0, Align.center, true);
		f.draw(spr, "Acc: " + String.valueOf(accuracy) + "%", x + PADDING, y - height + f.getLineHeight());
		f.draw(spr, "Crit: " + String.valueOf(critProb) + "%", x + width - PADDING, y - height + f.getLineHeight(), 0, Align.right, true);
		spr.end();
	}
	
}