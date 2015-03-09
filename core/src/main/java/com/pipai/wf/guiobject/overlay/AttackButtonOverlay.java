package com.pipai.wf.guiobject.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pipai.wf.renderable.BatchHelper;
import com.pipai.wf.renderable.Renderable;
import com.pipai.wf.renderable.gui.BattleTestGUI;
import com.pipai.wf.renderable.gui.LeftClickable;

public class AttackButtonOverlay implements Renderable, LeftClickable {
	
	private final int SQUARE_SIZE = 30;
	private BattleTestGUI gui;
	
	private int x, y;
	
	public AttackButtonOverlay(BattleTestGUI gui) {
		this.gui = gui;
	}
	
	public void onLeftClick(int screenX, int screenY, int gameX, int gameY) {
		
	}
	
	private void update(int screenW, int screenH) {
		x = screenW/2;
		y = 30;
	}

	public void render(BatchHelper batch, int width, int height) {
		update(width, height);
		ShapeRenderer r = batch.getShapeRenderer();
		r.begin(ShapeType.Filled);
		r.setColor(new Color(0, 0.5f, 0.8f, 1));
		r.rect(x - SQUARE_SIZE/2, y - SQUARE_SIZE/2, SQUARE_SIZE, SQUARE_SIZE);
		r.end();
	}
	
}