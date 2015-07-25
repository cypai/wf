package com.pipai.wf.guiobject.battle;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.EnvironmentObject;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.BattleGUI;
import com.pipai.wf.guiobject.GUIObject;
import com.pipai.wf.guiobject.Renderable;
import com.pipai.wf.guiobject.RightClickable3D;

public class BattleTerrainRenderer extends GUIObject implements Renderable, RightClickable3D {

	public static final int SQUARE_SIZE = 40;
	private static final Color MOVE_COLOR = new Color(0.5f, 0.5f, 1, 0.5f);
	private static final Color TARGET_COLOR = new Color(0.5f, 0, 0, 0.5f);
	private static final Color TARGETABLE_COLOR = new Color(1f, 0.8f, 0, 0.5f);
	private static final Color SOLID_COLOR = new Color(0, 0, 0, 1);
	
	public static GridPosition gamePosToGridPos(int gameX, int gameY) {
		int x_offset = gameX % SQUARE_SIZE;
		int y_offset = gameY % SQUARE_SIZE;
		return new GridPosition((gameX - x_offset)/SQUARE_SIZE, (gameY - y_offset)/SQUARE_SIZE);
	}
	
	public static Vector2 centerOfGridPos(GridPosition pos) {
		return new Vector2(pos.x*SQUARE_SIZE + SQUARE_SIZE/2, pos.y*SQUARE_SIZE + SQUARE_SIZE/2);
	}
	
	protected BattleGUI gui;
	protected BattleMap map;
	protected List<GridPosition> moveTiles, targetTiles, targetableTiles;
	
	private Environment environment;
	private Model boxModel;
	private ArrayList<ModelInstance> models;
	private ModelBuilder modelBuilder;

	public BattleTerrainRenderer(BattleGUI gui, BattleMap map) {
		super(gui);
		this.gui = gui;
		this.map = map;
		models = new ArrayList<ModelInstance>();
		modelBuilder = new ModelBuilder();
		boxModel = modelBuilder.createBox(SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, 
				new Material(ColorAttribute.createDiffuse(Color.GRAY)),
				Usage.Position | Usage.Normal);
		models.add(new ModelInstance(boxModel));
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, 0.8f, -0.2f));
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
		this.drawGrid(batch.getShapeRenderer(), 0, 0, SQUARE_SIZE * map.getCols(), SQUARE_SIZE * map.getRows(), map.getCols(), map.getRows());
		this.drawWalls(batch.getShapeRenderer());
		this.drawMovableTiles(batch.getShapeRenderer());
		this.drawTargetTiles(batch.getShapeRenderer());
		this.drawTargetableTiles(batch.getShapeRenderer());
		batch.getModelBatch().begin(gui.getCamera().getCamera());
		batch.getModelBatch().render(models, environment);
		batch.getModelBatch().end();
	}
	
	public void clearShadedTiles() {
		this.moveTiles = null;
		this.targetTiles = null;
		this.targetableTiles = null;
	}
	
	public void setMovableTiles(List<GridPosition> moveTiles) {
		this.moveTiles = moveTiles;
	}
	
	public void setTargetTiles(List<GridPosition> targetTiles) {
		this.targetTiles = targetTiles;
	}
	
	public void setTargetableTiles(List<GridPosition> targetableTiles) {
		this.targetableTiles = targetableTiles;
	}
	

//	private void renderShape(ShapeRenderer batch) {
//		this.drawGrid(batch, 0, 0, SQUARE_SIZE * map.getCols(), SQUARE_SIZE * map.getRows(), map.getCols(), map.getRows());
//		this.drawWalls(batch);
////		if (this.mode != Mode.ANIMATION && !aiTurn) {
////			this.drawMovableTiles(batch);
////		}
////		if (this.mode == Mode.TARGET_SELECT) {
////			this.drawTargetTiles(batch);
////		}
//	}
	
	private void drawGrid(ShapeRenderer batch, float x, float y, float width, float height, int numCols, int numRows) {
		batch.begin(ShapeType.Filled);
		batch.setColor(1, 1, 1, 1);
		batch.rect(x, y, width, height);
		batch.end();
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
	}
	
	private void drawWalls(ShapeRenderer batch) {
		// Needs optimization later
		for (int x=0; x<map.getCols(); x++) {
			for (int y=0; y<map.getRows(); y++) {
				GridPosition pos = new GridPosition(x, y);
				EnvironmentObject env = map.getCell(pos).getTileEnvironmentObject();
				if (env != null) {
					this.shadeSquare(batch, pos, SOLID_COLOR);
				}
			}
		}
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
