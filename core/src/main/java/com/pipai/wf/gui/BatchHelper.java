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
		decal = null;
	}

	public SpriteBatch getSpriteBatch() {
		return spr;
	}

	public ShapeRenderer getShapeRenderer() {
		return shape;
	}

	public ModelBatch getModelBatch() {
		return model;
	}

	public BitmapFont getFont() {
		return font;
	}

	public void set3DCamera(Camera cam) {
		decal = new DecalBatch(new CameraGroupStrategy(cam));
	}

	public DecalBatch getDecalBatch() {
		return decal;
	}

}