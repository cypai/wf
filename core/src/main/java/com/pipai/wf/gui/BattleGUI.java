package com.pipai.wf.gui;

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
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.WFGame;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.BattleObserver;
import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.action.RangeAttackAction;
import com.pipai.wf.battle.action.ReloadAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.ai.AI;
import com.pipai.wf.battle.ai.AIMoveRunnable;
import com.pipai.wf.battle.ai.OverwatchAI;
import com.pipai.wf.battle.attack.SimpleRangedAttack;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapGraph;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.guiobject.GUIObject;
import com.pipai.wf.guiobject.LeftClickable;
import com.pipai.wf.guiobject.Renderable;
import com.pipai.wf.guiobject.RightClickable;
import com.pipai.wf.guiobject.battle.AgentGUIObject;
import com.pipai.wf.guiobject.battle.BulletGUIObject;
import com.pipai.wf.guiobject.overlay.AttackButtonOverlay;
import com.pipai.wf.guiobject.overlay.TemporaryText;

/*
 * Simple 2D GUI for rendering a BattleMap
 */

public class BattleGUI extends GUI implements BattleObserver {
    
	public static final int SQUARE_SIZE = 40;
	private static final Color MOVE_COLOR = new Color(0.5f, 0.5f, 1, 0.5f);
	//private static final Color ATTACK_COLOR = new Color(0.5f, 0, 0, 0.5f);
	private static final Color SOLID_COLOR = new Color(0, 0, 0, 1);
	private static final int AI_MOVE_WAIT_TIME = 60;

	private OrthographicCamera camera, overlayCamera, orthoCamera;
	private BattleController battle;
	private HashMap<Agent, AgentGUIObject> agentMap;
	private ArrayList<AgentGUIObject> agentList;
	private LinkedList<AgentGUIObject> selectableAgentOrderedList;
	private AgentGUIObject selectedAgent;
	private MapGraph selectedMapGraph;
	private ArrayList<Renderable> renderables, foregroundRenderables, renderablesCreateBuffer, renderablesDelBuffer, overlayRenderables;
	private ArrayList<LeftClickable> leftClickables, leftClickablesCreateBuffer, leftClickablesDelBuffer, overlayLeftClickables;
	private ArrayList<RightClickable> rightClickables, rightClickablesCreateBuffer, rightClickablesDelBuffer;
	private boolean animating, allowInput, aiTurn;
	private int aiMoveWait = 0;
	private float cameraMoveTime = 1;
	private Vector3 cameraDest = null;
	private AI ai;

	public static GridPosition gamePosToGridPos(int gameX, int gameY) {
		int x_offset = gameX % SQUARE_SIZE;
		int y_offset = gameY % SQUARE_SIZE;
		return new GridPosition((gameX - x_offset)/SQUARE_SIZE, (gameY - y_offset)/SQUARE_SIZE);
	}
	
	public static Vector2 centerOfGridPos(GridPosition pos) {
		return new Vector2(pos.x*SQUARE_SIZE + SQUARE_SIZE/2, pos.y*SQUARE_SIZE + SQUARE_SIZE/2);
	}
	
	public BattleGUI(WFGame game, BattleMap map) {
		super(game);
        camera = new OrthographicCamera();
        overlayCamera = new OrthographicCamera();
        orthoCamera = new OrthographicCamera();
        camera.setToOrtho(false, this.getScreenWidth(), this.getScreenHeight());
        overlayCamera.setToOrtho(false, this.getScreenWidth(), this.getScreenHeight());
        orthoCamera.setToOrtho(false, this.getScreenWidth(), this.getScreenHeight());
		this.battle = new BattleController(map);
		this.battle.registerObserver(this);
		this.ai = new OverwatchAI(battle);
		this.animating = false;
		this.allowInput = true;
		this.aiTurn = false;
		this.renderables = new ArrayList<Renderable>();
		this.foregroundRenderables = new ArrayList<Renderable>();
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
		this.agentMap = new HashMap<Agent, AgentGUIObject>();
		this.agentList = new ArrayList<AgentGUIObject>();
		this.selectableAgentOrderedList = new LinkedList<AgentGUIObject>();
		for (Agent agent : this.battle.getBattleMap().getAgents()) {
			GridPosition pos = agent.getPosition();
			AgentGUIObject a = new AgentGUIObject(this, agent, (float)pos.x * SQUARE_SIZE + SQUARE_SIZE/2, (float)pos.y * SQUARE_SIZE + SQUARE_SIZE/2, SQUARE_SIZE/2);
			this.agentMap.put(agent, a);
			this.agentList.add(a);
			if (agent.getTeam() == Team.PLAYER) {
				this.selectableAgentOrderedList.add(a);
			}
			this.renderables.add(a);
			this.leftClickables.add(a);
			this.rightClickables.add(a);
		}
		AttackButtonOverlay atkBtn = new AttackButtonOverlay(this);
		this.overlayRenderables.add(atkBtn);
		this.overlayLeftClickables.add(atkBtn);
		this.setSelected(this.selectableAgentOrderedList.getFirst());
	}
	
	private void beginAnimation() { animating = true; }
	public void endAnimation() {
		animating = false;
		this.populateSelectableAgentList();
		this.performPostInputChecks();
	}
	
	public void setSelected(AgentGUIObject agent) {
		if (agent.getAgent().getAP() > 0) {
			this.selectedAgent = agent;
			this.moveCameraToPos(this.selectedAgent.x, this.selectedAgent.y);
			this.updatePaths();
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
			if (o.renderPriority() == -1) {
				foregroundRenderables.add(o);
			} else {
				renderables.add(o);
			}
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
			if (!renderables.remove(o)) {
				foregroundRenderables.remove(o);
			}
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
	
	@Override
	public void notifyBattleEvent(BattleEvent ev) {
		this.animateEvent(ev);
		this.aiMoveWait = 0;
	}
	
	private void performPostInputChecks() {
		if (this.selectedAgent.getAgent().getAP() == 0 || this.selectedAgent.getAgent().isKO()) {
			this.selectableAgentOrderedList.remove(this.selectedAgent);
			if (this.selectableAgentOrderedList.size() > 0) {
				this.setSelected(this.selectableAgentOrderedList.getFirst());
			}
		}
		for (AgentGUIObject a : this.agentList) {
			if (a.getAgent().getTeam() == Team.PLAYER && (a.getAgent().getAP() > 0 && !a.getAgent().isKO())) {
				return;
			}
		}
		this.allowInput = false;
		this.aiTurn = true;
		this.aiMoveWait = 0;
		this.battle.endTurn();
		this.ai.startTurn();
	}
	
	private void runAiTurn() {
		this.aiMoveWait += 1;
		if (this.aiMoveWait == BattleGUI.AI_MOVE_WAIT_TIME) {
			AIMoveRunnable t = new AIMoveRunnable(this.ai);
			t.run();
		}
	}
	
	private void populateSelectableAgentList() {
		this.selectableAgentOrderedList.clear();
		for (Agent agent : this.battle.getBattleMap().getAgents()) {
			if (agent.getTeam() == Team.PLAYER && agent.getAP() > 0 && !agent.isKO()) {
				this.selectableAgentOrderedList.add(this.agentMap.get(agent));
			}
		}
	}
	
	public void attack(AgentGUIObject target) {
		if (selectedAgent != null) {
			if (selectedAgent.getAgent().getCurrentWeapon().currentAmmo() > 0) {
				RangeAttackAction atk = new RangeAttackAction(selectedAgent.getAgent(), target.getAgent(), new SimpleRangedAttack());
				try {
					this.battle.performAction(atk);
				} catch (IllegalActionException e) {
					System.out.println("Illegal move: " + e.getMessage());
				}
			}
		}
	}
	
	public void onLeftClick(int screenX, int screenY) {
		if (!this.allowInput) {
			return;
		}
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
			this.performPostInputChecks();
		}
	}
	
	public void onRightClick(int screenX, int screenY) {
		if (!this.allowInput) {
			return;
		}
		Vector2 gamePos = screenPosToGraphicPos(screenX, screenY);
		int gameX = (int)gamePos.x;
		int gameY = (int)gamePos.y;
		if (!animating) {
			if (this.selectedAgent != null) {
				GridPosition clickSquare = gamePosToGridPos(gameX, gameY);
				if (this.selectedMapGraph.canMoveTo(clickSquare)) {
					LinkedList<GridPosition> path = selectedMapGraph.getPath(clickSquare);
					MoveAction move = new MoveAction(selectedAgent.getAgent(), path);
					try {
						this.battle.performAction(move);
					} catch (IllegalActionException e) {
						System.out.println("IllegalMoveException detected: " + e.getMessage());
					}
				}
			}
			for (RightClickable o : rightClickables) {
				o.onRightClick(gameX, gameY);
			}
			this.performPostInputChecks();
		}
	}
	
	public void animateEvent(BattleEvent event) {
		AgentGUIObject a = null, t;
		switch (event.getType()) {
		case MOVE:
			a = this.agentMap.get(event.getPerformer());
			beginAnimation();
			a.animateMoveSequence(vectorizePath(event.getPath()), event.getChainEvents());
			this.updatePaths();
			break;
		case ATTACK:
		case OVERWATCH_ACTIVATION:
			a = this.agentMap.get(event.getPerformer());
			t = this.agentMap.get(event.getTarget());
			BulletGUIObject b = new BulletGUIObject(this, a.x, a.y, t.x, t.y, t, event);
			this.createInstance(b);
			break;
		case OVERWATCH:
			a = this.agentMap.get(event.getPerformer());
			TemporaryText ttext = new TemporaryText(this, a.x, a.y, 80, 24, "Overwatch");
			this.createInstance(ttext);
			break;
		case START_TURN:
			if (event.getTeam() == Team.PLAYER) {
				this.allowInput = true;
				this.aiTurn = false;
				this.populateSelectableAgentList();
				this.setSelected(this.selectableAgentOrderedList.getFirst());
			}
			break;
		default:
			break;
		}
		if (aiTurn && a != null) {
			this.moveCameraToPos(a.x, a.y);
		}
	}
	
	private void globalUpdate() {
		updateCamera();
	}
	
	private void moveCameraToPos(float x, float y) {
		this.cameraMoveTime = 0;
		this.cameraDest = new Vector3(x, y, this.camera.position.z);
	}
	
	private void updateCamera() {
		if (this.cameraMoveTime < 1.0) {
			this.cameraMoveTime += 0.005;
			this.camera.position.interpolate(this.cameraDest, this.cameraMoveTime, Interpolation.linear);
			this.orthoCamera.position.interpolate(this.cameraDest, this.cameraMoveTime, Interpolation.linear);
		}
		if (this.allowInput) {
			if (this.checkKey(Keys.A)) {
				this.cameraMoveTime = 1;
				this.camera.translate(-3, 0);
				this.orthoCamera.translate(-3, 0);
			}
			if (this.checkKey(Keys.D)) {
				this.cameraMoveTime = 1;
				this.camera.translate(3, 0);
				this.orthoCamera.translate(3, 0);
			}
			if (this.checkKey(Keys.W)) {
				this.cameraMoveTime = 1;
				this.camera.translate(0, 3);
				this.orthoCamera.translate(0, 3);
			}
			if (this.checkKey(Keys.S)) {
				this.cameraMoveTime = 1;
				this.camera.translate(0, -3);
				this.orthoCamera.translate(0, -3);
			}
		}
        this.camera.update();
        this.orthoCamera.update();
	}
	
	@Override
	public void onKeyDown(int keycode) {
		if (keycode == Keys.ESCAPE) {
			Gdx.app.exit();
		}
		if (!this.allowInput || selectedAgent == null) {
			return;
		}
		Action action = null;
		switch (keycode) {
		case Keys.R:
			// Reload
			action = new ReloadAction(selectedAgent.getAgent());
			break;
		case Keys.C:
			// Ready spell
			break;
		case Keys.Y:
			// Overwatch
			action = new OverwatchAction(selectedAgent.getAgent(), new SimpleRangedAttack());
			break;
		case Keys.K:
			// Hunker
			break;
		case Keys.M:
			// Recharge mana
			break;
		case Keys.SHIFT_LEFT:
			// Select next unit
			this.selectableAgentOrderedList.remove(this.selectedAgent);
			this.selectableAgentOrderedList.addLast(this.selectedAgent);
			this.setSelected(this.selectableAgentOrderedList.peek());
		default:
			break;
		}
		if (action != null) {
			try {
				this.battle.performAction(action);
			} catch (IllegalActionException e) {
				System.out.println("Illegal move: " + e.getMessage());
			}
		}
		this.performPostInputChecks();
	}
	
	@Override
	public void onKeyUp(int keycode) {
		
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
		for (Renderable r : this.foregroundRenderables) {
			r.render(batch);
		}
        batch.getSpriteBatch().setProjectionMatrix(overlayCamera.combined);
        batch.getShapeRenderer().setProjectionMatrix(overlayCamera.combined);
		for (Renderable r : this.overlayRenderables) {
			r.render(batch);
		}
		if (aiTurn) {
			runAiTurn();
		}
		cleanDelBuffers();
		cleanCreateBuffers();
	}
	
	private void renderShape(ShapeRenderer batch) {
		BattleMap map = this.battle.getBattleMap();
		this.drawGrid(batch, 0, 0, SQUARE_SIZE * map.getCols(), SQUARE_SIZE * map.getRows(), map.getCols(), map.getRows());
		this.drawWalls(batch);
		if (!animating && !aiTurn) {
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
		for (AgentGUIObject a : this.agentList) {
			drawAgentInfo(batch, a);
		}
	}
	
	private void drawAgentInfo(ShapeRenderer batch, AgentGUIObject a) {
		if (a.getAgent().isKO()) {
			return;
		}
		batch.begin(ShapeType.Filled);
		int bar_width = 40;
		Vector2 agentPoint = new Vector2(a.x, a.y);
		Vector2 barLeftTop = new Vector2(a.x + 24, a.y + 24);
		Vector2 barRightFullTop = new Vector2(a.x + 24 + bar_width, a.y + 24);
		Vector2 barRightTop = new Vector2(a.x + 24 + bar_width * ((float)a.getAgent().getArmor().getHP() / (float)a.getAgent().getArmor().maxHP()), a.y + 24);
		Vector2 barLeftBot = new Vector2(a.x + 24, a.y + 18);
		Vector2 barRightFullBot = new Vector2(a.x + 24 + bar_width, a.y + 18);
		Vector2 barRightBot = new Vector2(a.x + 24 + bar_width * ((float)a.getAgent().getHP() / (float)a.getAgent().getMaxHP()), a.y + 18);
		batch.setColor(Color.BLUE);
		batch.rectLine(agentPoint, barLeftTop, 3);
		// Health bar background
		batch.setColor(Color.BLACK);
		batch.rectLine(barLeftTop, barRightFullTop, 6);
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
		batch.rect(barLeftTop.x, barLeftTop.y + 3, barRightFullTop.x - barLeftTop.x, -12);
		batch.end();
		// Overwatch Icon
		if (a.getAgent().isOverwatching()) {
			batch.begin(ShapeType.Filled);
			batch.setColor(Color.GRAY);
			batch.circle(barRightFullTop.x + 8, (barRightFullTop.y + barRightFullBot.y)/2, 6);
			batch.end();
		}
	}

}