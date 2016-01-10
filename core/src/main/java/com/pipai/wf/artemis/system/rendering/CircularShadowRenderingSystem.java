package com.pipai.wf.artemis.system.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.managers.TagManager;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.artemis.components.CircularShadowComponent;
import com.pipai.wf.artemis.components.PerspectiveCameraComponent;
import com.pipai.wf.artemis.components.VisibleComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;
import com.pipai.wf.artemis.system.Tag;

public class CircularShadowRenderingSystem extends IteratingSystem {

	private static final int SQUARE_SIZE = 40;

	private ComponentMapper<XYZPositionComponent> mXyz;
	private ComponentMapper<CircularShadowComponent> mCircularShadow;
	private ComponentMapper<VisibleComponent> mVisible;
	private ComponentMapper<PerspectiveCameraComponent> mPerspectiveCamera;

	private BatchRenderingSystem batchRenderingSystem;

	private Pixmap pixmap;
	private Texture circleTexture;

	private TagManager tagManager;

	public CircularShadowRenderingSystem() {
		super(Aspect.all(XYZPositionComponent.class, CircularShadowComponent.class, VisibleComponent.class));
	}

	@Override
	protected void initialize() {
		pixmap = new Pixmap(SQUARE_SIZE, SQUARE_SIZE, Pixmap.Format.RGBA8888);
		pixmap.setColor(new Color(0, 0, 0, 0));
		pixmap.fill();
		pixmap.setColor(new Color(0, 0, 0, 0.5f));
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
			XYZPositionComponent position = mXyz.get(entityId);
			VisibleComponent visibility = mVisible.get(entityId);
			CircularShadowComponent cShadow = mCircularShadow.get(entityId);
			if (visibility.visible) {
				// TODO: Add this during initialization
				if (cShadow.shadowDecal == null) {
					cShadow.shadowDecal = Decal.newDecal(new TextureRegion(circleTexture), true);
				}
				PerspectiveCamera camera = mPerspectiveCamera.get(tagManager.getEntity(Tag.CAMERA.toString())).camera;
				cShadow.shadowDecal.setDimensions(32, 32);
				Vector3 shadowPos = new Vector3(position.position.x, position.position.y, 0.1f);
				cShadow.shadowDecal.setPosition(shadowPos);
				cShadow.shadowDecal.setRotation(new Vector3(0, 0, 1), camera.up);
				DecalBatch batch = batchRenderingSystem.getBatch().getDecalBatch();
				batch.add(cShadow.shadowDecal);
			}
		}
	}

	@Override
	protected void dispose() {
		circleTexture.dispose();
		pixmap.dispose();
	}

}
