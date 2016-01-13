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
import com.pipai.wf.artemis.components.TileHighlightComponent;

public class TileHighlightRenderingSystem extends IteratingSystem {

	private ComponentMapper<TileHighlightComponent> mTileHighlight;

	private BatchRenderingSystem batchRenderingSystem;

	private Texture highlightTexture;

	public TileHighlightRenderingSystem() {
		super(Aspect.all(TileHighlightComponent.class));
	}

	@Override
	protected void initialize() {
		Pixmap pixmap = new Pixmap(40, 40, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		highlightTexture = new Texture(pixmap);
		pixmap.dispose();
	}

	@Override
	protected void process(int entityId) {
		TileHighlightComponent cTileHighlight = mTileHighlight.get(entityId);
		Decal highlightDecal = Decal.newDecal(new TextureRegion(highlightTexture), true);
		highlightDecal.setColor(cTileHighlight.color);
		highlightDecal.setPosition(new Vector3(cTileHighlight.tile.getX() * 40 + 20, cTileHighlight.tile.getY() * 40 + 20, 0.1f));
		DecalBatch batch = batchRenderingSystem.getBatch().getDecalBatch();
		batch.add(highlightDecal);
	}

}
