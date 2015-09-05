package com.pipai.wf.guiobject.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.BattleGui;
import com.pipai.wf.guiobject.GuiObject;
import com.pipai.wf.guiobject.GuiRenderable;
import com.pipai.wf.guiobject.overlay.TemporaryText;
import com.pipai.wf.util.UtilFunctions;

public class BulletGuiObject extends GuiObject implements GuiRenderable {

	protected BattleGui gui;
	protected float x, y, dest_x, dest_y;
	protected int t, final_t;
	protected Vector2 dir;
	protected AgentGuiObject target;
	protected BattleEvent outcome;

	private static final int SPEED = 16;

	public BulletGuiObject(BattleGui gui, float x, float y, float dest_x, float dest_y, AgentGuiObject target, BattleEvent outcome) {
		super(gui);
		this.gui = gui;
		this.x = x;
		this.y = y;
		this.dest_x = dest_x;
		this.dest_y = dest_y;
		dir = new Vector2(dest_x - x, dest_y - y);
		dir.nor();
		final_t = (int)Math.ceil((dest_x - x)/(SPEED * dir.x));
		if (final_t == 0) {
			final_t = (int)Math.ceil((dest_y - y)/(SPEED * dir.y));
		}
		this.target = target;
		this.outcome = outcome;
	}

	@Override
	public int renderPriority() { return 0; }

	@Override
	public void update() {
		t += 1;
		if (t < final_t) {
			x += SPEED * dir.x;
			y += SPEED * dir.y;
		} else if (t == final_t) {
			x = dest_x;
			y = dest_y;
			TemporaryText dmgTxt;
			Vector3 txtAnchor = new Vector3(x + 12 , y + 16*UtilFunctions.rng.nextFloat() - 12, 0);
			if (outcome.isHit()) {
				dmgTxt = new TemporaryText(gui, txtAnchor, (outcome.isCrit() ? "/!\\ " : "Hit: ") + String.valueOf(outcome.getDamage()));
			} else {
				dmgTxt = new TemporaryText(gui, txtAnchor, "Missed");
			}
			gui.createInstance(dmgTxt);
			destroy();
			target.hit(outcome);
		}
	}

	@Override
	public void render(BatchHelper batch) {
		update();
		ShapeRenderer r = batch.getShapeRenderer();
		r.begin(ShapeType.Filled);
		r.setColor(Color.GREEN);
		r.circle(x, y, 5);
		r.end();
		r.begin(ShapeType.Line);
		r.setColor(Color.BLACK);
		r.circle(x, y, 5);
		r.end();
	}
}