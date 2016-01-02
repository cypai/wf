package com.pipai.wf.guiobject.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;
import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.component.CritAccuracyComponent;
import com.pipai.wf.battle.action.component.HitAccuracyComponent;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.BattleGui;
import com.pipai.wf.guiobject.GuiObject;
import com.pipai.wf.guiobject.GuiRenderable;

public class ActionToolTip extends GuiObject implements GuiRenderable {

	private static final int PADDING = 6;

	private BattleGui gui;
	private Mode mode;
	private float x, y, width, height;
	private String title, description;
	private int accuracy, critProb;

	public ActionToolTip(BattleGui gui, float x, float y, float width, float height) {
		super(gui);
		this.gui = gui;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public int renderPriority() {
		return 0;
	}

	public void setToGeneralDescription(String title, String description) {
		mode = Mode.GENERAL;
		this.title = title;
		this.description = description;
	}

	public void setToActionDescription(Action a) {
		boolean hasHit = a instanceof HitAccuracyComponent;
		boolean hasCrit = a instanceof CritAccuracyComponent;
		if (hasHit) {
			accuracy = ((HitAccuracyComponent) a).toHit();
		}
		if (hasCrit) {
			critProb = ((CritAccuracyComponent) a).toCrit();
		}
		if (hasHit || hasCrit) {
			mode = Mode.ACCURACY_ACTION;
		} else {
			mode = Mode.GENERAL;
		}
		title = a.getName();
		description = a.getDescription();
	}

	@Override
	public void render(BatchHelper batch) {
		if (gui.getMode() != BattleGui.Mode.TARGET_SELECT) {
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
		f.draw(spr, title, (x + width) / 2, y - f.getLineHeight(), 0, Align.center, true);
		f.draw(spr, description, x, y - 2.5f * f.getLineHeight(), width - PADDING * 2, Align.center, true);
		if (mode == Mode.ACCURACY_ACTION) {
			f.draw(spr, "Acc: " + String.valueOf(accuracy) + "%", x + PADDING, y - height + f.getLineHeight());
			f.draw(spr, "Crit: " + String.valueOf(critProb) + "%", x + width - PADDING, y - height + f.getLineHeight(), 0, Align.right, true);
		}
		spr.end();
	}

	public enum Mode {
		GENERAL, ACCURACY_ACTION;
	}

}
