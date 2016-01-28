package com.pipai.wf.artemis.system.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.pipai.wf.artemis.components.WorldMapComponent;

public class WorldMapRenderingSystem extends IteratingSystem {

	public WorldMapRenderingSystem() {
		super(Aspect.all(WorldMapComponent.class));
	}

	private ComponentMapper<WorldMapComponent> mWorldMap;
	// private ComponentMapper<OrthographicCameraComponent> mOrthoCamera;

	// private TagManager tagManager;

	@Override
	protected void process(int entityId) {
		// OrthographicCameraComponent cOrtho = mOrthoCamera.get(tagManager.getEntity(Tag.ORTHO_CAMERA.toString()));
		WorldMapComponent cWorldMap = mWorldMap.get(entityId);
		cWorldMap.renderer.render();
	}

}
