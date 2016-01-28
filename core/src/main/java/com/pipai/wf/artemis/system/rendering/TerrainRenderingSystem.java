package com.pipai.wf.artemis.system.rendering;

import java.util.ArrayList;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.DirectionalLightsAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.pipai.wf.artemis.components.PerspectiveCameraComponent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.CoverType;
import com.pipai.wf.battle.map.EnvironmentObject;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.guiobject.TerrainMeshGenerator;
import com.pipai.wf.util.GridPosition;

public class TerrainRenderingSystem extends IteratingSystem {

	private static final int SQUARE_SIZE = 40;

	private ComponentMapper<PerspectiveCameraComponent> mPerspectiveCamera;

	private BattleMap map;

	private BatchHelper batch;
	private Environment environment;
	private Model boxModel;
	private Mesh terrainMesh;
	private ArrayList<ModelInstance> wallModels;
	// private ModelBuilder modelBuilder;
	private Texture grassTexture;
	private ShaderProgram fogOfWarShader;

	private AssetManager assets;

	private Texture fogOfWarStateTexture;
	private Pixmap visibilityPixmap;

	// SUPPRESS CHECKSTYLE MemberNames these match with style in GLSL
	private int u_projViewTrans, u_worldTrans, u_lightColor;

	// SUPPRESS CHECKSTYLE MemberNames these match with style in GLSL
	private int u_texture, u_fogOfWarTexture;

	public TerrainRenderingSystem(BatchHelper batch, BattleMap map) {
		super(Aspect.all(PerspectiveCameraComponent.class));
		this.batch = batch;
		this.map = map;
		String vert = Gdx.files.internal("graphics/shaders/fogofwar.vertex.glsl").readString();
		String frag = Gdx.files.internal("graphics/shaders/fogofwar.fragment.glsl").readString();
		fogOfWarShader = new ShaderProgram(vert, frag);
		if (!fogOfWarShader.isCompiled()) {
			throw new GdxRuntimeException(fogOfWarShader.getLog());
		}
		u_projViewTrans = fogOfWarShader.getUniformLocation("u_projViewTrans");
		u_worldTrans = fogOfWarShader.getUniformLocation("u_worldTrans");
		u_lightColor = fogOfWarShader.getUniformLocation("u_lightColor");
		u_texture = fogOfWarShader.getUniformLocation("u_texture");
		u_fogOfWarTexture = fogOfWarShader.getUniformLocation("u_fogOfWarTexture");
		terrainMesh = TerrainMeshGenerator.generateMeshFromBattleMap(map, SQUARE_SIZE);
		grassTexture = new Texture(Gdx.files.internal("graphics/textures/grass.png"));
		wallModels = new ArrayList<ModelInstance>();
		// modelBuilder = new ModelBuilder();
		// boxModel = modelBuilder.createBox(SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE,
		// new Material(ColorAttribute.createDiffuse(Color.GRAY)),
		// Usage.Position | Usage.Normal);
		environment = new Environment();
		environment.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.8f));
		visibilityPixmap = new Pixmap(map.getCols(), map.getRows(), Format.RGBA8888);
		visibilityPixmap.setColor(Color.WHITE);
		visibilityPixmap.fill();
		fogOfWarStateTexture = new Texture(visibilityPixmap);

		assets = new AssetManager();
		assets.load("graphics/models/tree.g3dj", Model.class);
		while (!assets.update()) {
			//
		}
		generateSceneModels();
	}

	private void generateSceneModels() {
		Model treeModel = assets.get("graphics/models/tree.g3dj", Model.class);
		for (int r = 0; r < map.getRows(); r++) {
			for (int c = 0; c < map.getCols(); c++) {
				EnvironmentObject env = map.getCell(new GridPosition(c, r)).getTileEnvironmentObject();
				if (env != null && env.getCoverType() == CoverType.FULL) {
					ModelInstance treeInstance = new ModelInstance(treeModel);
					treeInstance.transform.setToTranslation(c * SQUARE_SIZE + SQUARE_SIZE / 2,
							r * SQUARE_SIZE + SQUARE_SIZE / 2, SQUARE_SIZE / 2);
					treeInstance.transform.rotate(Vector3.X, 90);
					treeInstance.transform.scale(10, 10, 10);
					wallModels.add(treeInstance);
					// wallModels.add(new ModelInstance(boxModel, c *
					// SQUARE_SIZE + SQUARE_SIZE / 2,
					// r * SQUARE_SIZE + SQUARE_SIZE / 2, SQUARE_SIZE / 2));
				}
			}
		}
	}

	@Override
	protected void process(int entityId) {
		PerspectiveCamera camera = mPerspectiveCamera.get(entityId).camera;
		ModelBatch modelBatch = batch.getModelBatch();
		Gdx.gl20.glCullFace(GL20.GL_BACK);
		fogOfWarShader.begin();
		fogOfWarShader.setUniformMatrix(u_projViewTrans, camera.combined);
		fogOfWarShader.setUniformMatrix(u_worldTrans, new Matrix4());
		DirectionalLightsAttribute envlights = (DirectionalLightsAttribute) environment
				.get(DirectionalLightsAttribute.Type);
		fogOfWarShader.setUniformf(u_lightColor, envlights.lights.first().color);
		grassTexture.bind(0);
		fogOfWarShader.setUniformi(u_texture, 0);
		fogOfWarStateTexture.bind(1);
		fogOfWarShader.setUniformi(u_fogOfWarTexture, 1);
		terrainMesh.render(fogOfWarShader, GL20.GL_TRIANGLES);
		fogOfWarShader.end();
		modelBatch.begin(camera);
		modelBatch.render(wallModels, environment);
		modelBatch.end();
	}

	@Override
	protected void dispose() {
		fogOfWarStateTexture.dispose();
		visibilityPixmap.dispose();
		boxModel.dispose();
		grassTexture.dispose();
		terrainMesh.dispose();
	}

}
