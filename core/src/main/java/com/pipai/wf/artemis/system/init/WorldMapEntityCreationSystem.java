package com.pipai.wf.artemis.system.init;

import com.artemis.ComponentMapper;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.pipai.wf.artemis.components.OrthographicCameraComponent;
import com.pipai.wf.artemis.components.WorldMapComponent;
import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.artemis.system.Tag;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.world.map.WorldMap;

/**
 * This system creates all initial entities for the Battle Screen
 */
public class WorldMapEntityCreationSystem extends NoProcessingSystem {

	// private static final Logger LOGGER = LoggerFactory.getLogger(WorldMapEntityCreationSystem.class);

	private ComponentMapper<OrthographicCameraComponent> mOrthoCamera;
	private ComponentMapper<WorldMapComponent> mWorldMap;

	private TagManager tagManager;

	private final BatchHelper batch;

	public WorldMapEntityCreationSystem(BatchHelper batch) {
		this.batch = batch;
	}

	@Override
	protected void initialize() {
		OrthographicCamera camera = generateCameras();
		generateMapEntities(camera);
	}

	private OrthographicCamera generateCameras() {
		int orthoCameraId = world.create();
		OrthographicCameraComponent cCamera = mOrthoCamera.create(orthoCameraId);
		tagManager.register(Tag.ORTHO_CAMERA.toString(), orthoCameraId);
		batch.getShapeRenderer().setProjectionMatrix(cCamera.camera.combined);
		batch.getSpriteBatch().setProjectionMatrix(cCamera.camera.combined);
		return cCamera.camera;
	}

	private void generateMapEntities(OrthographicCamera camera) {
		int worldMapId = world.create();
		WorldMapComponent cWorldMap = mWorldMap.create(worldMapId);
		cWorldMap.worldMap = new WorldMap();
		cWorldMap.renderer = new OrthogonalTiledMapRenderer(cWorldMap.worldMap.getTiledMap());
		cWorldMap.renderer.setView(camera);
	}

}
