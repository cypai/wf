package com.pipai.wf.renderable.gui;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.action.RangeAttackAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.attack.SimpleRangedAttack;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapGraph;
import com.pipai.wf.guiobject.overlay.AttackButtonOverlay;
import com.pipai.wf.guiobject.test.AgentTestGUIObject;
import com.pipai.wf.guiobject.test.BulletTestGUIObject;
import com.pipai.wf.renderable.BatchHelper;
import com.pipai.wf.renderable.Renderable;

/*
 * Simple 2D GUI for rendering a BattleMap
 */

public class BattleTestGUI implements Renderable {
    
	public static final int SQUARE_SIZE = 40;
	private static final Color MOVE_COLOR = new Color(0.5f, 0.5f, 1, 0.5f);
	private static final Color ATTACK_COLOR = new Color(0.5f, 0, 0, 0.5f);
	private static final Color SOLID_COLOR = new Color(0, 0, 0, 1);
	
	private BattleController battle;
	private AgentTestGUIObject selectedAgent;
	private MapGraph selectedMapGraph;
	private ArrayList<Renderable> renderables, renderablesDelBuffer, overlayRenderables;
	private ArrayList<LeftClickable> leftClickables, leftClickablesDelBuffer, overlayLeftClickables;
	private ArrayList<RightClickable> rightClickables, rightClickablesDelBuffer;
	private boolean animating;

	public static boolean withinGridBounds(GridPosition pos, int gameX, int gameY) {
		return (gameX > pos.x * SQUARE_SIZE) && (gameX < (pos.x+1) * SQUARE_SIZE) && (gameY > pos.y * SQUARE_SIZE) && (gameY < (pos.y+1) * SQUARE_SIZE);
	}
	
	public static GridPosition gamePosToGridPos(int gameX, int gameY) {
		int x_offset = gameX % SQUARE_SIZE;
		int y_offset = gameY % SQUARE_SIZE;
		return new GridPosition((gameX - x_offset)/SQUARE_SIZE, (gameY - y_offset)/SQUARE_SIZE);
	}
	
	public static Vector2 centerOfGridPos(GridPosition pos) {
		return new Vector2(pos.x*SQUARE_SIZE + SQUARE_SIZE/2, pos.y*SQUARE_SIZE + SQUARE_SIZE/2);
	}
	
	public BattleTestGUI(BattleController battle) {
		this.battle = battle;
		this.animating = false;
		this.renderables = new ArrayList<Renderable>();
		this.leftClickables = new ArrayList<LeftClickable>();
		this.rightClickables = new ArrayList<RightClickable>();
		this.overlayRenderables = new ArrayList<Renderable>();
		this.overlayLeftClickables = new ArrayList<LeftClickable>();
		this.renderablesDelBuffer = new ArrayList<Renderable>();
		this.leftClickablesDelBuffer = new ArrayList<LeftClickable>();
		this.rightClickablesDelBuffer = new ArrayList<RightClickable>();
		for (Agent agent : this.battle.getBattleMap().getAgents()) {
			GridPosition pos = agent.getPosition();
			AgentTestGUIObject a = new AgentTestGUIObject(this, agent, (float)pos.x * SQUARE_SIZE + SQUARE_SIZE/2, (float)pos.y * SQUARE_SIZE + SQUARE_SIZE/2);
			this.renderables.add(a);
			this.leftClickables.add(a);
			this.rightClickables.add(a);
		}
		AttackButtonOverlay atkBtn = new AttackButtonOverlay(this);
		this.overlayRenderables.add(atkBtn);
		this.overlayLeftClickables.add(atkBtn);
	}
	
	private void beginAnimation() { animating = true; }
	public void endAnimation() { animating = false; }
	
	public void setSelected(AgentTestGUIObject agent) {
		this.selectedAgent = agent;
		this.runPathfinding();
	}
	
	public void attack(AgentTestGUIObject target) {
		if (selectedAgent != null) {
			RangeAttackAction atk = new RangeAttackAction(selectedAgent.getAgent(), target.getAgent(), new SimpleRangedAttack());
			this.battle.performAction(atk);
			BulletTestGUIObject b = new BulletTestGUIObject(this, selectedAgent.x, selectedAgent.y, target.x, target.y, target);
			renderables.add(b);
		}
	}
	
	public void remove(Object o) {
		if (o instanceof Renderable) {
			renderablesDelBuffer.add((Renderable)o);
		}
		if (o instanceof LeftClickable) {
			leftClickablesDelBuffer.add((LeftClickable)o);
		}
		if (o instanceof RightClickable) {
			rightClickablesDelBuffer.add((RightClickable)o);
		}
	}
	
	private void cleanDelBuffers() {
		for (Renderable o : renderablesDelBuffer) {
			renderables.remove(o);
		}
		for (LeftClickable o : leftClickablesDelBuffer) {
			leftClickables.remove(o);
		}
		for (RightClickable o : rightClickablesDelBuffer) {
			rightClickables.remove(o);
		}
		renderablesDelBuffer.clear();
		leftClickablesDelBuffer.clear();
		rightClickablesDelBuffer.clear();
	}
	
	public void updatePaths() { runPathfinding(); }
	
	private void runPathfinding() {
		if (selectedAgent != null) {
			MapGraph graph = new MapGraph(this.battle.getBattleMap(), selectedAgent.getAgent().getPosition(), selectedAgent.getAgent().getMobility(), 1);
			this.selectedMapGraph = graph;
		}
	}
	
	private LinkedList<Vector2> vectorizePath(LinkedList<GridPosition> path) {
		LinkedList<Vector2> vectorized = new LinkedList<Vector2>();
		for (GridPosition p : path) {
			vectorized.add(centerOfGridPos(p));
		}
		return vectorized;
	}
	
	public void onLeftClick(int screenX, int screenY, int gameX, int gameY) {
		if (!animating) {
			for (LeftClickable o : overlayLeftClickables) {
				o.onLeftClick(screenX, screenY, gameX, gameY);
			}
			for (LeftClickable o : leftClickables) {
				o.onLeftClick(screenX, screenY, gameX, gameY);
			}
		}
	}
	
	public void onRightClick(int screenX, int screenY, int gameX, int gameY) {
		if (!animating) {
			if (this.selectedAgent != null) {
				GridPosition clickSquare = gamePosToGridPos(gameX, gameY);
				if (this.selectedMapGraph.canMoveTo(clickSquare)) {
					MoveAction move = new MoveAction(selectedAgent.getAgent(), clickSquare);
					this.battle.performAction(move);
					beginAnimation();
					selectedAgent.animateMoveSequence(vectorizePath(selectedMapGraph.getPath(clickSquare)));
					this.runPathfinding();
				}
			}
			for (RightClickable o : rightClickables) {
				o.onRightClick(gameX, gameY);
			}
		}
	}
	
	public void render(BatchHelper batch, int width, int height) {
		renderShape(batch.getShapeRenderer());
		for (Renderable r : this.renderables) {
			r.render(batch, width, height);
		}
		for (Renderable r : this.overlayRenderables) {
			r.render(batch, width, height);
		}
		cleanDelBuffers();
	}
	
	public void renderOverlay(BatchHelper batch, int width, int height) {
	}
	
	private void renderShape(ShapeRenderer batch) {
		BattleMap map = this.battle.getBattleMap();
		this.drawGrid(batch, 0, 0, SQUARE_SIZE * map.getCols(), SQUARE_SIZE * map.getRows(), map.getCols(), map.getRows());
		this.drawWalls(batch);
		if (!animating) {
			this.drawMovableTiles(batch);
		}
	}
	
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
		BattleMap map = this.battle.getBattleMap();
		// Needs optimization later
		for (int x=0; x<map.getCols(); x++) {
			for (int y=0; y<map.getRows(); y++) {
				GridPosition pos = new GridPosition(x, y);
				if (map.getCell(pos).isSolid()) {
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
		if (this.selectedMapGraph != null) {
			ArrayList<GridPosition> tileList = this.selectedMapGraph.getMovableCellPositions();
			for (GridPosition pos : tileList) {
				this.shadeSquare(batch, pos, MOVE_COLOR);
			}
		}
	}

}