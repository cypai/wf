package com.pipai.wf.guiobject.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.BattleGui;
import com.pipai.wf.guiobject.GuiObject;
import com.pipai.wf.guiobject.GuiRenderable;
import com.pipai.wf.guiobject.XYZPositioned;

public class BulletGuiObject extends GuiObject implements GuiRenderable, XYZPositioned {

	protected BattleGui gui;
	protected float x, y, dest_x, dest_y;
	protected int t, final_t;
	protected Vector2 dir;
	protected AgentGuiObject target;

	private static final int SPEED = 16;

	public BulletGuiObject(BattleGui gui, float x, float y, float dest_x, float dest_y, AgentGuiObject target) {
		super(gui);
		this.gui = gui;
		this.x = x;
		this.y = y;
		this.dest_x = dest_x;
		this.dest_y = dest_y;
		dir = new Vector2(dest_x - x, dest_y - y);
		dir.nor();
		final_t = (int) Math.ceil((dest_x - x) / (SPEED * dir.x));
		if (final_t == 0) {
			final_t = (int) Math.ceil((dest_y - y) / (SPEED * dir.y));
		}
		this.target = target;
	}

	@Override
	public int renderPriority() {
		return 0;
	}

	@Override
	public void update() {
		t += 1;
		if (t < final_t) {
			x += SPEED * dir.x;
			y += SPEED * dir.y;
		} else if (t == final_t) {
			x = dest_x;
			y = dest_y;
			destroy();
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

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	@Override
	public float getZ() {
		return 0;
	}

	@Override
	public void setZ(float z) {
		return;
	}

	@Override
	public Vector3 getPosition() {
		return new Vector3(x, y, 0);
	}
}