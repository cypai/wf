package com.pipai.wf.renderable.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.pipai.wf.WFGame;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.action.RangeAttackAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.attack.SimpleRangedAttack;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapGraph;
import com.pipai.wf.guiobject.GUIObject;
import com.pipai.wf.guiobject.overlay.AttackButtonOverlay;
import com.pipai.wf.guiobject.test.AgentTestGUIObject;
import com.pipai.wf.guiobject.test.BulletTestGUIObject;
import com.pipai.wf.renderable.Renderable;

/*
 * Simple 2D GUI for rendering a BattleMap
 */

public class BattleTestGUI extends GUI {
    
	public static final int SQUARE_SIZE = 40;
	private static final Color MOVE_COLOR = new Color(0.5f, 0.5f, 1, 0.5f);
	//private static final Color ATTACK_COLOR = new Color(0.5f, 0, 0, 0.5f);
	private static final Color SOLID_COLOR = new Color(0, 0, 0, 1);

	private OrthographicCamera camera, overlayCamera, orthoCamera;
	private BattleController battle;
	private HashMap<Agent, AgentTestGUIObject> agentMap;
	private ArrayList<AgentTestGUIObject> agentList;
	private AgentTestGUIObject selectedAgent;
	private MapGraph selectedMapGraph;
	private ArrayList<Renderable> renderables, renderablesCreateBuffer, renderablesDelBuffer, overlayRenderables;
	private ArrayList<LeftClickable> leftClickables, leftClickablesCreateBuffer, leftClickablesDelBuffer, overlayLeftClickables;
	private ArrayList<RightClickable> rightClickables, rightClickablesCreateBuffer, rightClickablesDelBuffer;
	private boolean animating, overlayClicked;

	public static GridPosition gamePosToGridPos(int gameX, int gameY) {
		int x_offset = gameX % SQUARE_SIZE;
		int y_offset = gameY % SQUARE_SIZE;
		return new GridPosition((gameX - x_offset)/SQUARE_SIZE, (gameY - y_offset)/SQUARE_SIZE);
	}
	
	public static Vector2 centerOfGridPos(GridPosition pos) {
		return new Vector2(pos.x*SQUARE_SIZE + SQUARE_SIZE/2, pos.y*SQUARE_SIZE + SQUARE_SIZE/2);
	}
	
	public BattleTestGUI(WFGame game) {
		super(game);
        BattleMap map = new BattleMap(12, 10);
        map.addAgent(new AgentState(new GridPosition(1, 1), Agent.Team.PLAYER, 5));
        map.addAgent(new AgentState(new GridPosition(4, 1), Agent.Team.PLAYER, 5));
        map.addAgent(new AgentState(new GridPosition(5, 8), Agent.Team.ENEMY, 5));
        map.addAgent(new AgentState(new GridPosition(9, 10), Agent.Team.ENEMY, 5));
        map.getCell(new GridPosition(5, 5)).setSolid(true);
        camera = new OrthographicCamera();
        overlayCamera = new OrthographicCamera();
        orthoCamera = new OrthographicCamera();
        camera.setToOrtho(false, this.getScreenWidth(), this.getScreenHeight());
        overlayCamera.setToOrtho(false, this.getScreenWidth(), this.getScreenHeight());
        orthoCamera.setToOrtho(false, this.getScreenWidth(), this.getScreenHeight());
		this.battle = new BattleController(map);
		this.animating = false;
		this.renderables = new ArrayList<Renderable>();
		this.leftClickables = new ArrayList<LeftClickable>();
		this.rightClickables = new ArrayList<RightClickable>();
		this.overlayRenderables = new ArrayList<Renderable>();
		this.overlayLeftClickables = new ArrayList<LeftClickable>();
		this.renderablesCreateBuffer = new ArrayList<Renderable>();
		this.leftClickablesCreateBuffer = new ArrayList<LeftClickable>();
		this.rightClickablesCreateBuffer = new ArrayList<RightClickable>();
		this.renderablesDelBuffer = new ArrayList<Renderable>();
		this.leftClickablesDelBuffer = new ArrayList<LeftClickable>();
		this.rightClickablesDelBuffer = new ArrayList<RightClickable>();
		this.agentMap = new HashMap<Agent, AgentTestGUIObject>();
		this.agentList = new ArrayList<AgentTestGUIObject>();
		for (Agent agent : this.battle.getBattleMap().getAgents()) {
			GridPosition pos = agent.getPosition();
			AgentTestGUIObject a = new AgentTestGUIObject(this, agent, (float)pos.x * SQUARE_SIZE + SQUARE_SIZE/2, (float)pos.y * SQUARE_SIZE + SQUARE_SIZE/2, SQUARE_SIZE/2);
			this.agentMap.put(agent, a);
			this.agentList.add(a);
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
	public void setOverlayClickedFlag() { overlayClicked = true; }
	
	public void setSelected(AgentTestGUIObject agent) {
		this.selectedAgent = agent;
		this.updatePaths();
	}
	
	public void attack(AgentTestGUIObject target) {
		if (selectedAgent != null) {
			RangeAttackAction atk = new RangeAttackAction(selectedAgent.getAgent(), target.getAgent(), new SimpleRangedAttack());
			BattleEvent outcome = this.battle.performAction(atk);
			System.out.println(outcome.toString());
			BulletTestGUIObject b = new BulletTestGUIObject(this, selectedAgent.x, selectedAgent.y, target.x, target.y, target, outcome.getDamageRoll());
			renderables.add(b);
		}
	}
	
	@Override
	public void createInstance(GUIObject o) {
		super.createInstance(o);
		if (o instanceof Renderable) {
			renderablesCreateBuffer.add((Renderable)o);
		}
		if (o instanceof LeftClickable) {
			leftClickablesCreateBuffer.add((LeftClickable)o);
		}
		if (o instanceof RightClickable) {
			rightClickablesCreateBuffer.add((RightClickable)o);
		}
	}
	
	@Override
	public void deleteInstance(GUIObject o) {
		super.deleteInstance(o);
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
	
	private void cleanCreateBuffers() {
		for (Renderable o : renderablesCreateBuffer) {
			renderables.add(o);
		}
		for (LeftClickable o : leftClickablesCreateBuffer) {
			leftClickables.add(o);
		}
		for (RightClickable o : rightClickablesCreateBuffer) {
			rightClickables.add(o);
		}
		renderablesCreateBuffer.clear();
		leftClickablesCreateBuffer.clear();
		rightClickablesCreateBuffer.clear();
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
	
	public void updatePaths() {
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

	private Vector2 screenPosToGraphicPos(int screenX, int screenY) {
		float x = camera.position.x - this.getScreenWidth()/2 + screenX;
		float y = camera.position.y - this.getScreenHeight()/2 + screenY;
		return new Vector2(x, y);
	}
	
	public void onLeftClick(int screenX, int screenY) {
		Vector2 gamePos = screenPosToGraphicPos(screenX, screenY);
		int gameX = (int)gamePos.x;
		int gameY = (int)gamePos.y;
		if (!animating) {
			for (LeftClickable o : overlayLeftClickables) {
				o.onLeftClick(screenX, screenY, gameX, gameY);
			}
			for (LeftClickable o : leftClickables) {
				o.onLeftClick(screenX, screenY, gameX, gameY);
			}
		}
	}
	
	public void onRightClick(int screenX, int screenY) {
		Vector2 gamePos = screenPosToGraphicPos(screenX, screenY);
		int gameX = (int)gamePos.x;
		int gameY = (int)gamePos.y;
		if (!animating) {
			if (this.selectedAgent != null) {
				GridPosition clickSquare = gamePosToGridPos(gameX, gameY);
				if (this.selectedMapGraph.canMoveTo(clickSquare)) {
					LinkedList<GridPosition> path = selectedMapGraph.getPath(clickSquare);
					MoveAction move = new MoveAction(selectedAgent.getAgent(), path);
					BattleEvent event = this.battle.performAction(move);
					animateEvent(event);
				}
			}
			for (RightClickable o : rightClickables) {
				o.onRightClick(gameX, gameY);
			}
		}
	}
	
	private void animateEvent(BattleEvent event) {
		switch (event.getType()) {
		case MOVE:
			AgentTestGUIObject a = this.agentMap.get(event.getPerformer());
			beginAnimation();
			a.animateMoveSequence(vectorizePath(event.getPath()));
			this.updatePaths();
			
		default:
			break;
		}
	}
	
	private void globalUpdate() {
		if (this.checkKey(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		updateCamera();
	}
	
	private void updateCamera() {
		if (this.checkKey(Keys.A)) {
			this.camera.translate(-3, 0);
			this.orthoCamera.translate(-3, 0);
		}
		if (this.checkKey(Keys.D)) {
			this.camera.translate(3, 0);
			this.orthoCamera.translate(3, 0);
		}
		if (this.checkKey(Keys.W)) {
			this.camera.translate(0, 3);
			this.orthoCamera.translate(0, 3);
		}
		if (this.checkKey(Keys.S)) {
			this.camera.translate(0, -3);
			this.orthoCamera.translate(0, -3);
		}
        this.camera.update();
        this.orthoCamera.update();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		this.camera.setToOrtho(false, width, height);
		this.orthoCamera.setToOrtho(false, width, height);
		this.overlayCamera.setToOrtho(false, width, height);
	}
	
	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		globalUpdate();
        batch.getSpriteBatch().setProjectionMatrix(camera.combined);
        batch.getShapeRenderer().setProjectionMatrix(camera.combined);
		renderShape(batch.getShapeRenderer());
		for (Renderable r : this.renderables) {
			r.render(batch);
		}
        batch.getSpriteBatch().setProjectionMatrix(orthoCamera.combined);
        batch.getShapeRenderer().setProjectionMatrix(orthoCamera.combined);
		this.drawAllAgentInfo(batch.getShapeRenderer());
        batch.getSpriteBatch().setProjectionMatrix(overlayCamera.combined);
        batch.getShapeRenderer().setProjectionMatrix(overlayCamera.combined);
		for (Renderable r : this.overlayRenderables) {
			r.render(batch);
		}
		cleanDelBuffers();
		cleanCreateBuffers();
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
	
	private void drawAllAgentInfo(ShapeRenderer batch) {
		for (AgentTestGUIObject a : this.agentList) {
			drawAgentInfo(batch, a);
		}
	}
	
	private void drawAgentInfo(ShapeRenderer batch, AgentTestGUIObject a) {
		if (a.getAgent().isKO()) {
			return;
		}
		batch.begin(ShapeType.Filled);
		int bar_width = 40;
		Vector2 agentPoint = new Vector2(a.x, a.y);
		Vector2 barLeftTop = new Vector2(a.x + 24, a.y + 24);
		Vector2 barRightTop = new Vector2(a.x + 24 + bar_width, a.y + 24);
		Vector2 barLeftBot = new Vector2(a.x + 24, a.y + 18);
		Vector2 barRightFullBot = new Vector2(a.x + 24 + bar_width, a.y + 18);
		Vector2 barRightBot = new Vector2(a.x + 24 + bar_width * ((float)a.getAgent().getHP() / (float)a.getAgent().getMaxHP()), a.y + 18);
		batch.setColor(Color.BLUE);
		batch.rectLine(agentPoint, barLeftTop, 3);
		// Health bar background
		batch.setColor(Color.BLACK);
		batch.rectLine(barLeftTop, barRightTop, 6);
		batch.rectLine(barLeftBot, barRightFullBot, 6);
		// Health bar
		batch.setColor(Color.GRAY);
		batch.rectLine(barLeftTop, barRightTop, 6);
		batch.setColor(Color.GREEN);
		batch.rectLine(barLeftBot, barRightBot, 6);
		batch.end();
		// Health bars outline
		batch.begin(ShapeType.Line);
		batch.setColor(Color.BLUE);
		batch.rect(barLeftTop.x, barLeftTop.y + 3, barRightTop.x - barLeftTop.x, -12);
		batch.end();
	}

}