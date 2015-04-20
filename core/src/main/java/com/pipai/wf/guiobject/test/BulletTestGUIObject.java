package com.pipai.wf.guiobject.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.pipai.wf.guiobject.GUIObject;
import com.pipai.wf.guiobject.overlay.TemporaryText;
import com.pipai.wf.renderable.BatchHelper;
import com.pipai.wf.renderable.Renderable;
import com.pipai.wf.renderable.gui.BattleTestGUI;

public class BulletTestGUIObject extends GUIObject implements Renderable {
	
	private BattleTestGUI gui;
	private float x, y, dest_x, dest_y;
	private int t, final_t;
	private Vector2 dir;
	private AgentTestGUIObject target;
	private int damage;
	
	private static final int SPEED = 16;
	
	public BulletTestGUIObject(BattleTestGUI gui, float x, float y, float dest_x, float dest_y, AgentTestGUIObject target, int damage) {
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
		this.damage = damage;
	}
	
	public void update() {
		t += 1;
		if (t < final_t) {
			x += SPEED * dir.x;
			y += SPEED * dir.y;
		} else if (t == final_t) {
			x = dest_x;
			y = dest_y;
			TemporaryText dmgTxt = new TemporaryText(gui, x, y, 24.0f, 24.0f, String.valueOf(damage));
			gui.createInstance(dmgTxt);
			gui.deleteInstance(this);
			target.hit();
		}
	}
	
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