package com.pipai.wf.guiobject.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.BattleGui;

public class FireballGuiObject extends BulletGuiObject {
	
	public FireballGuiObject(BattleGui gui, float x, float y, float dest_x, float dest_y, AgentGuiObject target, BattleEvent outcome) {
		super(gui, x, y, dest_x, dest_y, target, outcome);
	}
	
	public void render(BatchHelper batch) {
		super.update();
		ShapeRenderer r = batch.getShapeRenderer();
		r.begin(ShapeType.Filled);
		r.setColor(Color.RED);
		r.circle(x, y, 5);
		r.end();
		r.begin(ShapeType.Line);
		r.setColor(Color.ORANGE);
		r.circle(x, y, 5);
		r.end();
	}
	
}
