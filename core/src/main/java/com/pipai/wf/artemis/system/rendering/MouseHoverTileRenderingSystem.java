package com.pipai.wf.artemis.system.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.artemis.components.MouseHoverTileComponent;

public class MouseHoverTileRenderingSystem extends IteratingSystem {

	private ComponentMapper<MouseHoverTileComponent> mMouseTile;

	private BatchRenderingSystem batchRenderingSystem;

	private Texture mouseTileTexture;

	public MouseHoverTileRenderingSystem() {
		super(Aspect.all(MouseHoverTileComponent.class));
		Pixmap pixmap = new Pixmap(40, 40, Pixmap.Format.RGBA8888);
		pixmap.setColor(new Color(1, 1, 1, 0.7f));
		pixmap.drawRectangle(0, 0, 40, 40);
		pixmap.drawRectangle(1, 1, 38, 38);
		mouseTileTexture = new Texture(pixmap);
		pixmap.dispose();
	}

	@Override
	protected void process(int entityId) {
		MouseHoverTileComponent cMouseTile = mMouseTile.get(entityId);
		Decal highlightDecal = Decal.newDecal(new TextureRegion(mouseTileTexture), true);
		highlightDecal
				.setPosition(new Vector3(cMouseTile.tile.getX() * 40 + 20, cMouseTile.tile.getY() * 40 + 20, 0.2f));
		DecalBatch batch = batchRenderingSystem.getBatch().getDecalBatch();
		batch.add(highlightDecal);
	}

}
