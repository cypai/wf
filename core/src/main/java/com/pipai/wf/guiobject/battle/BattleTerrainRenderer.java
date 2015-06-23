package com.pipai.wf.guiobject.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
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
	private static final Color ATTACK_COLOR = new Color(0.5f, 0, 0, 0.5f);
	private static final Color TARGET_COLOR = new Color(1f, 0.8f, 0, 0.5f);
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

	public BattleTerrainRenderer(BattleGUI gui, BattleMap map) {
		super(gui);
		this.gui = gui;
		this.map = map;
	}

	@Override
	public boolean onRightClick(Ray ray) {
		return false;
	}

	@Override
	public int renderPriority() {
		return 0;
	}

	@Override
	public void render(BatchHelper batch) {
		this.drawGrid(batch.getShapeRenderer(), 0, 0, SQUARE_SIZE * map.getCols(), SQUARE_SIZE * map.getRows(), map.getCols(), map.getRows());
		this.drawWalls(batch.getShapeRenderer());
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
	
//	private void drawMovableTiles(ShapeRenderer batch) {
//		if (this.selectedMapGraph != null) {
//			ArrayList<GridPosition> tileList = this.selectedMapGraph.getMovableCellPositions();
//			for (GridPosition pos : tileList) {
//				this.shadeSquare(batch, pos, MOVE_COLOR);
//			}
//		}
//	}
//	
//	private void drawTargetTiles(ShapeRenderer batch) {
//		for (AgentGUIObject target : this.targetAgentList) {
//			if (target == this.targetAgent) {
//				this.shadeSquare(batch, target.getAgent().getPosition(), TARGET_COLOR);
//			} else {
//				this.shadeSquare(batch, target.getAgent().getPosition(), ATTACK_COLOR);
//			}
//		}
//	}
}
