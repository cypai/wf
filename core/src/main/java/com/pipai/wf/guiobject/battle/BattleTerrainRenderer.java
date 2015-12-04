package com.pipai.wf.guiobject.battle;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.DirectionalLightsAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.CoverType;
import com.pipai.wf.battle.map.EnvironmentObject;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapGraph;
import com.pipai.wf.battle.vision.FogOfWar;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.BattleGui;
import com.pipai.wf.guiobject.GuiObject;
import com.pipai.wf.guiobject.GuiRenderable;
import com.pipai.wf.guiobject.RightClickable3D;

public class BattleTerrainRenderer extends GuiObject implements GuiRenderable, RightClickable3D {

	public static final int SQUARE_SIZE = 40;
	private static final Color MOVE_COLOR = new Color(0.5f, 0.5f, 1, 0.5f);
	private static final Color MID_DASH_COLOR = new Color(0.8f, 0.8f, 0, 0.5f);
	private static final Color DASH_COLOR = new Color(1f, 0.8f, 0, 0.5f);
	private static final Color TARGET_COLOR = new Color(0.5f, 0, 0, 0.5f);
	private static final Color TARGETABLE_COLOR = new Color(1f, 0.8f, 0, 0.5f);

	public static GridPosition gamePosToGridPos(float gameX, float gameY) {
		int gameX_i = (int) gameX;
		int gameY_i = (int) gameY;
		int x_offset = gameX_i % SQUARE_SIZE;
		int y_offset = gameY_i % SQUARE_SIZE;
		return new GridPosition((gameX_i - x_offset) / SQUARE_SIZE, (gameY_i - y_offset) / SQUARE_SIZE);
	}

	public static Vector2 centerOfGridPos(GridPosition pos) {
		return new Vector2(pos.x * SQUARE_SIZE + SQUARE_SIZE / 2, pos.y * SQUARE_SIZE + SQUARE_SIZE / 2);
	}

	private BattleGui gui;
	private BattleMap map;
	private List<GridPosition> movableTiles, midDashTiles, dashTiles, targetTiles, targetableTiles;
	private FogOfWar fogOfWar;

	private Environment environment;
	private Model boxModel;
	private Mesh terrainMesh;
	private ArrayList<ModelInstance> wallModels;
	private ModelBuilder modelBuilder;
	private Texture grassTexture;
	private ShaderProgram fogOfWarShader;
	private Renderable renderable;

	private int u_projViewTrans, u_worldTrans, u_lightColor;
	private int u_texture, u_fogOfWarTexture;

	private boolean renderingTextures = true;

	public BattleTerrainRenderer(BattleGui gui, BattleMap map, FogOfWar fogOfWar) {
		super(gui);
		this.gui = gui;
		this.map = map;
		this.fogOfWar = fogOfWar;
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
		modelBuilder = new ModelBuilder();
		boxModel = modelBuilder.createBox(SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE,
				new Material(ColorAttribute.createDiffuse(Color.GRAY)),
				Usage.Position | Usage.Normal);
		environment = new Environment();
		environment.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.8f));
		generateSceneModels();
	}

	public void setRenderingTextures(boolean renderTex) {
		renderingTextures = renderTex;
	}

	public boolean isRenderingTextures() {
		return renderingTextures;
	}

	private void generateSceneModels() {
		for (int r = 0; r < map.getRows(); r++) {
			for (int c = 0; c < map.getCols(); c++) {
				EnvironmentObject env = map.getCell(new GridPosition(c, r)).getTileEnvironmentObject();
				if (env != null && env.getCoverType() == CoverType.FULL) {
					wallModels.add(new ModelInstance(boxModel, c * SQUARE_SIZE + SQUARE_SIZE / 2, r * SQUARE_SIZE + SQUARE_SIZE / 2, SQUARE_SIZE / 2));
				}
			}
		}
	}

	@Override
	public void dispose() {
		boxModel.dispose();
	}

	@Override
	public boolean onRightClick(Ray ray) {
		float t = -ray.origin.z / ray.direction.z;
		Vector3 endpoint = new Vector3();
		ray.getEndPoint(endpoint, t);
		GridPosition dest = gamePosToGridPos((int) endpoint.x, (int) endpoint.y);
		gui.performMoveWithSelectedAgent(dest);
		return true;
	}

	@Override
	public int renderPriority() {
		return 0;
	}

	public void updateFogOfWar() {

	}

	@Override
	public void render(BatchHelper batch) {
		ModelBatch modelBatch = batch.getModelBatch();
		if (renderingTextures) {
			// Gdx.gl20.glCullFace(GL20.GL_BACK);
			fogOfWarShader.begin();
			fogOfWarShader.setUniformMatrix(u_projViewTrans, gui.getCamera().getProjectionMatrix());
			fogOfWarShader.setUniformMatrix(u_worldTrans, new Matrix4());
			DirectionalLightsAttribute envlights = (DirectionalLightsAttribute) environment.get(DirectionalLightsAttribute.Type);
			fogOfWarShader.setUniformf(u_lightColor, envlights.lights.first().color);
			grassTexture.bind(0);
			fogOfWarShader.setUniformi(u_texture, 0);
			fogOfWar.getFogOfWarStateTexture().bind(1);
			fogOfWarShader.setUniformi(u_fogOfWarTexture, 1);
			terrainMesh.render(fogOfWarShader, GL20.GL_TRIANGLES);
			fogOfWarShader.end();
		}
		drawGrid(batch.getShapeRenderer(), 0, 0, SQUARE_SIZE * map.getCols(), SQUARE_SIZE * map.getRows(), map.getCols(), map.getRows());
		drawMovableTiles(batch.getShapeRenderer());
		drawMidDashTiles(batch.getShapeRenderer());
		drawDashTiles(batch.getShapeRenderer());
		drawTargetableTiles(batch.getShapeRenderer());
		drawTargetTiles(batch.getShapeRenderer());
		modelBatch.begin(gui.getCamera().getCamera());
		modelBatch.render(wallModels, environment);
		modelBatch.end();
	}

	public void clearShadedTiles() {
		movableTiles = null;
		midDashTiles = null;
		dashTiles = null;
		targetTiles = null;
		targetableTiles = null;
	}

	public void setMovingTiles(MapGraph mapgraph) {
		clearShadedTiles();
		int ap = mapgraph.getAP();
		int apmax = mapgraph.getAPMax();
		if (ap == 1) {
			setDashTiles(mapgraph.getMovableCellPositions(1));
			return;
		}
		if (apmax == 2) {
			setMovableTiles(mapgraph.getMovableCellPositions(1));
			setDashTiles(mapgraph.getMovableCellPositions(2));
		} else if (apmax == 3) {
			if (ap == 2) {
				setMidDashTiles(mapgraph.getMovableCellPositions(1));
				setDashTiles(mapgraph.getMovableCellPositions(2));
			} else {
				setMovableTiles(mapgraph.getMovableCellPositions(1));
				setMidDashTiles(mapgraph.getMovableCellPositions(2));
				setDashTiles(mapgraph.getMovableCellPositions(3));
			}
		}
	}

	public void setMovableTiles(List<GridPosition> moveTiles) {
		movableTiles = moveTiles;
	}

	public void setMidDashTiles(List<GridPosition> midDashTiles) {
		this.midDashTiles = midDashTiles;
	}

	public void setDashTiles(List<GridPosition> dashTiles) {
		this.dashTiles = dashTiles;
	}

	public void setTargetTiles(List<GridPosition> targetTiles) {
		this.targetTiles = targetTiles;
	}

	public void setTargetableTiles(List<GridPosition> targetableTiles) {
		this.targetableTiles = targetableTiles;
	}

	private void drawGrid(ShapeRenderer batch, float x, float y, float width, float height, int numCols, int numRows) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.begin(ShapeType.Line);
		batch.setColor(0, 0.7f, 0.7f, 0.5f);
		for (int i = 0; i <= numCols; i++) {
			float horiz_pos = x + i * width / numCols;
			batch.line(horiz_pos, y, horiz_pos, y + height);
		}
		for (int i = 0; i <= numRows; i++) {
			float vert_pos = y + i * height / numRows;
			batch.line(x, vert_pos, x + width, vert_pos);
		}
		batch.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	private void shadeSquare(ShapeRenderer batch, GridPosition pos, Color color) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.begin(ShapeType.Filled);
		batch.setColor(color);
		batch.rect(pos.x * SQUARE_SIZE, pos.y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
		batch.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	private void drawMovableTiles(ShapeRenderer batch) {
		if (movableTiles != null) {
			for (GridPosition pos : movableTiles) {
				shadeSquare(batch, pos, MOVE_COLOR);
			}
		}
	}

	private void drawMidDashTiles(ShapeRenderer batch) {
		if (midDashTiles != null) {
			for (GridPosition pos : midDashTiles) {
				shadeSquare(batch, pos, MID_DASH_COLOR);
			}
		}
	}

	private void drawDashTiles(ShapeRenderer batch) {
		if (dashTiles != null) {
			for (GridPosition pos : dashTiles) {
				shadeSquare(batch, pos, DASH_COLOR);
			}
		}
	}

	private void drawTargetTiles(ShapeRenderer batch) {
		if (targetTiles != null) {
			for (GridPosition pos : targetTiles) {
				shadeSquare(batch, pos, TARGET_COLOR);
			}
		}
	}

	private void drawTargetableTiles(ShapeRenderer batch) {
		if (targetableTiles != null) {
			for (GridPosition pos : targetableTiles) {
				shadeSquare(batch, pos, TARGETABLE_COLOR);
			}
		}
	}

}
