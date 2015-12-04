package com.pipai.wf.gui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BatchHelper {

	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private DecalBatch decalBatch;
	private ModelBatch modelBatch;

	public BatchHelper(SpriteBatch spr, ShapeRenderer shape, ModelBatch model, BitmapFont font) {
		spriteBatch = spr;
		shapeRenderer = shape;
		modelBatch = model;
		this.font = font;
		decalBatch = null;
	}

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	public ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}

	public ModelBatch getModelBatch() {
		return modelBatch;
	}

	public BitmapFont getFont() {
		return font;
	}

	public void set3DCamera(Camera cam) {
		decalBatch = new DecalBatch(new CameraGroupStrategy(cam));
	}

	public DecalBatch getDecalBatch() {
		return decalBatch;
	}

}
