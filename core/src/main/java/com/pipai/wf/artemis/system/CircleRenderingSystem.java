package com.pipai.wf.artemis.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.pipai.wf.artemis.components.CircleDecalComponent;
import com.pipai.wf.artemis.components.VisibleComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;
import com.pipai.wf.gui.BatchHelper;

public class CircleRenderingSystem extends IteratingSystem {

	private static final int SQUARE_SIZE = 40;

	ComponentMapper<XYZPositionComponent> xyzMapper;
	ComponentMapper<CircleDecalComponent> circleMapper;
	ComponentMapper<VisibleComponent> visibleMapper;

	private PerspectiveCamera camera;
	private DecalBatch batch;
	private Pixmap pixmap;
	private Texture circleTexture;

	public CircleRenderingSystem(BatchHelper batch, PerspectiveCamera camera) {
		super(Aspect.all(XYZPositionComponent.class, CircleDecalComponent.class, VisibleComponent.class));
		this.camera = camera;
		this.batch = batch.getDecalBatch();
	}

	@Override
	protected void initialize() {
		pixmap = new Pixmap(SQUARE_SIZE, SQUARE_SIZE, Pixmap.Format.RGBA8888);
		pixmap.fill();
		pixmap.setColor(Color.RED);
		pixmap.fillCircle(SQUARE_SIZE / 2, SQUARE_SIZE / 2, SQUARE_SIZE / 2 - 1);
		circleTexture = new Texture(pixmap);
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void process(int entityId) {
		if (checkProcessing()) {
			XYZPositionComponent position = xyzMapper.get(entityId);
			VisibleComponent visibility = visibleMapper.get(entityId);
			CircleDecalComponent circleDecal = circleMapper.get(entityId);
			if (visibility.visible) {
				if (circleDecal.decal == null) {
					circleDecal.decal = Decal.newDecal(new TextureRegion(circleTexture), true);
				}
				circleDecal.decal.setDimensions(32, 32);
				circleDecal.decal.setPosition(position.position);
				circleDecal.decal.setRotation(camera.direction, camera.up);
				batch.add(circleDecal.decal);
			}
		}
	}

	@Override
	protected void end() {
		batch.flush();
	}

	@Override
	protected void dispose() {
		circleTexture.dispose();
		pixmap.dispose();
	}

}
