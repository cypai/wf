package com.pipai.wf.guiobject.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.GUI;
import com.pipai.wf.guiobject.GUIObject;
import com.pipai.wf.guiobject.LeftClickable;
import com.pipai.wf.guiobject.Renderable;
import com.pipai.wf.util.UtilFunctions;

public class AttackButtonOverlay extends GUIObject implements Renderable, LeftClickable {
	
	private final int SQUARE_SIZE = 30;
	
	private int x, y;
	
	public AttackButtonOverlay(GUI gui) {
		super(gui);
	}
	
	public int renderPriority() { return 0; }
	
	public void onLeftClick(int screenX, int screenY, int gameX, int gameY) {
		if (UtilFunctions.isInBoundingBox(x, y, SQUARE_SIZE, SQUARE_SIZE, screenX, screenY)) {
			System.out.println("hi");
		}
	}
	
	private void update(int screenW, int screenH) {
		x = screenW/2;
		y = 30;
	}

	public void render(BatchHelper batch) {
		update(gui.getScreenWidth(), gui.getScreenHeight());
		ShapeRenderer r = batch.getShapeRenderer();
		r.begin(ShapeType.Filled);
		r.setColor(new Color(0, 0.5f, 0.8f, 1));
		r.rect(x - SQUARE_SIZE/2, y - SQUARE_SIZE/2, SQUARE_SIZE, SQUARE_SIZE);
		r.end();
		SpriteBatch spr = batch.getSpriteBatch();
		BitmapFont font = batch.getFont();
		spr.begin();
		font.setColor(Color.WHITE);
		font.draw(spr, "A", x - SQUARE_SIZE/4, y + SQUARE_SIZE/4);
		spr.end();
	}
	
}