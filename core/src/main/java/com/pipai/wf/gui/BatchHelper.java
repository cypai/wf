package com.pipai.wf.gui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BatchHelper {
	
	private SpriteBatch spr;
	private ShapeRenderer shape;
	private BitmapFont font;
	private DecalBatch decal;
	private ModelBatch model;
	
	public BatchHelper(SpriteBatch spr, ShapeRenderer shape, ModelBatch model, BitmapFont font) {
		this.spr = spr;
		this.shape = shape;
		this.model = model;
		this.font = font;
		this.decal = null;
	}
	
	public SpriteBatch getSpriteBatch() { return this.spr; }
	public ShapeRenderer getShapeRenderer() { return this.shape; }
	public ModelBatch getModelBatch() { return this.model; }
	public BitmapFont getFont() { return this.font; }
	
	public void set3DCamera(Camera cam) {
		this.decal = new DecalBatch(new CameraGroupStrategy(cam));
	}
	public DecalBatch getDecalBatch() {
		return this.decal;
	}
	
}