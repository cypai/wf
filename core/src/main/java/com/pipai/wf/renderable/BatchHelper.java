package com.pipai.wf.renderable;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BatchHelper {
	private SpriteBatch spr;
	private ShapeRenderer shape;
	private BitmapFont font;
	
	public BatchHelper(SpriteBatch spr, ShapeRenderer shape, BitmapFont font) {
		this.spr = spr;
		this.shape = shape;
		this.font = font;
	}
	
	public SpriteBatch getSpriteBatch() { return this.spr; }
	public ShapeRenderer getShapeRenderer() { return this.shape; }
	public BitmapFont getFont() { return this.font; }
	
}