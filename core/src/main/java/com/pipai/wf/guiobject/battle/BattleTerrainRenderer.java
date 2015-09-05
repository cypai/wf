package com.pipai.wf.guiobject.battle;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.CoverType;
import com.pipai.wf.battle.map.EnvironmentObject;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapGraph;
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

	public static GridPosition gamePosToGridPos(int gameX, int gameY) {
		int x_offset = gameX % SQUARE_SIZE;
		int y_offset = gameY % SQUARE_SIZE;
		return new GridPosition((gameX - x_offset)/SQUARE_SIZE, (gameY - y_offset)/SQUARE_SIZE);
	}

	public static Vector2 centerOfGridPos(GridPosition pos) {
		return new Vector2(pos.x*SQUARE_SIZE + SQUARE_SIZE/2, pos.y*SQUARE_SIZE + SQUARE_SIZE/2);
	}

	protected BattleGui gui;
	protected BattleMap map;
	protected List<GridPosition> moveTiles, midDashTiles, dashTiles, targetTiles, targetableTiles;

	private Environment environment;
	private Model boxModel, terrainModel;
	private ArrayList<ModelInstance> terrainModels, wallModels;
	private ModelBuilder modelBuilder;
	private Texture grassTexture;

	public BattleTerrainRenderer(BattleGui gui, BattleMap map) {
		super(gui);
		this.gui = gui;
		this.map = map;
		grassTexture = new Texture(Gdx.files.internal("graphics/textures/grass.png"));
		terrainModels = new ArrayList<ModelInstance>();
		wallModels = new ArrayList<ModelInstance>();
		modelBuilder = new ModelBuilder();
		boxModel = modelBuilder.createBox(SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE,
				new Material(ColorAttribute.createDiffuse(Color.GRAY)),
				Usage.Position | Usage.Normal);
		TextureRegion grassRegion = new TextureRegion(grassTexture, 0, 0, 128, 128);
		Material grassMat = new Material(TextureAttribute.createDiffuse(grassRegion));
		terrainModel = modelBuilder.createRect(0, 0, 0, SQUARE_SIZE, 0, 0, SQUARE_SIZE, SQUARE_SIZE, 0, 0, SQUARE_SIZE, 0,
				0, 0, 1,
				grassMat,
				Usage.Position | Usage.Normal | Usage.TextureCoordinates );
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.1f, 0.1f, 0.1f, 1f));
		environment.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.8f));
		generateSceneModels();
	}

	private void generateSceneModels() {
		for (int r = 0; r < map.getRows(); r++) {
			for (int c = 0; c < map.getCols(); c++) {
				terrainModels.add(new ModelInstance(terrainModel,c * SQUARE_SIZE, r * SQUARE_SIZE, 0));
				EnvironmentObject env = map.getCell(new GridPosition(c, r)).getTileEnvironmentObject();
				if (env != null && env.getCoverType() == CoverType.FULL) {
					wallModels.add(new ModelInstance(boxModel, c * SQUARE_SIZE + SQUARE_SIZE/2, r * SQUARE_SIZE + SQUARE_SIZE/2, SQUARE_SIZE/2));
				}
			}
		}
	}

	@Override
	public void dispose() {
		this.boxModel.dispose();
	}

	@Override
	public boolean onRightClick(Ray ray) {
		float t = -ray.origin.z/ray.direction.z;
		Vector3 endpoint = new Vector3();
		ray.getEndPoint(endpoint, t);
		GridPosition dest = gamePosToGridPos((int)endpoint.x, (int)endpoint.y);
		gui.performMoveWithSelectedAgent(dest);
		return true;
	}

	@Override
	public int renderPriority() {
		return 0;
	}

	@Override
	public void render(BatchHelper batch) {
		batch.getModelBatch().begin(gui.getCamera().getCamera());
		batch.getModelBatch().render(terrainModels, environment);
		batch.getModelBatch().end();
		this.drawGrid(batch.getShapeRenderer(), 0, 0, SQUARE_SIZE * map.getCols(), SQUARE_SIZE * map.getRows(), map.getCols(), map.getRows());
		this.drawMovableTiles(batch.getShapeRenderer());
		this.drawMidDashTiles(batch.getShapeRenderer());
		this.drawDashTiles(batch.getShapeRenderer());
		this.drawTargetableTiles(batch.getShapeRenderer());
		this.drawTargetTiles(batch.getShapeRenderer());
		batch.getModelBatch().begin(gui.getCamera().getCamera());
		batch.getModelBatch().render(wallModels, environment);
		batch.getModelBatch().end();
	}

	public void clearShadedTiles() {
		this.moveTiles = null;
		this.midDashTiles = null;
		this.dashTiles = null;
		this.targetTiles = null;
		this.targetableTiles = null;
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
		this.moveTiles = moveTiles;
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
		for (int i = 0; i<=numCols; i++) {
			float horiz_pos = x + i*width/numCols;
			batch.line(horiz_pos, y, horiz_pos, y + height);
		}
		for (int i = 0; i<=numRows; i++) {
			float vert_pos = y + i*height/numRows;
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
		if (this.moveTiles != null) {
			for (GridPosition pos : this.moveTiles) {
				this.shadeSquare(batch, pos, MOVE_COLOR);
			}
		}
	}

	private void drawMidDashTiles(ShapeRenderer batch) {
		if (this.midDashTiles != null) {
			for (GridPosition pos : this.midDashTiles) {
				this.shadeSquare(batch, pos, MID_DASH_COLOR);
			}
		}
	}

	private void drawDashTiles(ShapeRenderer batch) {
		if (this.dashTiles != null) {
			for (GridPosition pos : this.dashTiles) {
				this.shadeSquare(batch, pos, DASH_COLOR);
			}
		}
	}

	private void drawTargetTiles(ShapeRenderer batch) {
		if (this.targetTiles != null) {
			for (GridPosition pos : this.targetTiles) {
				this.shadeSquare(batch, pos, TARGET_COLOR);
			}
		}
	}

	private void drawTargetableTiles(ShapeRenderer batch) {
		if (this.targetableTiles != null) {
			for (GridPosition pos : this.targetableTiles) {
				this.shadeSquare(batch, pos, TARGETABLE_COLOR);
			}
		}
	}

}
