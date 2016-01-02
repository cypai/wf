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

	private float x, y, destX, destY;
	private int t, finalT;
	private Vector2 dir;

	private static final int SPEED = 16;

	public BulletGuiObject(BattleGui gui, float x, float y, float destX, float destY, AgentGuiObject target) {
		super(gui);
		this.x = x;
		this.y = y;
		this.destX = destX;
		this.destY = destY;
		dir = new Vector2(destX - x, destY - y);
		dir.nor();
		finalT = (int) Math.ceil((destX - x) / (SPEED * dir.x));
		if (finalT == 0) {
			finalT = (int) Math.ceil((destY - y) / (SPEED * dir.y));
		}
	}

	@Override
	public int renderPriority() {
		return 0;
	}

	@Override
	public void update() {
		t += 1;
		if (t < finalT) {
			x += SPEED * dir.x;
			y += SPEED * dir.y;
		} else if (t == finalT) {
			x = destX;
			y = destY;
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
		// Do nothing for now
	}

	@Override
	public Vector3 getPosition() {
		return new Vector3(x, y, 0);
	}

}
