package com.pipai.wf.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.pipai.wf.WFGame;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.BattleObserver;
import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.CastTargetAction;
import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.action.RangeAttackAction;
import com.pipai.wf.battle.action.ReadySpellAction;
import com.pipai.wf.battle.action.ReloadAction;
import com.pipai.wf.battle.action.SwitchWeaponAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.ai.AI;
import com.pipai.wf.battle.ai.AIMoveRunnable;
import com.pipai.wf.battle.ai.RandomAI;
import com.pipai.wf.battle.attack.Attack;
import com.pipai.wf.battle.attack.SimpleRangedAttack;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapGraph;
import com.pipai.wf.battle.spell.FireballSpell;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.battle.weapon.SpellWeapon;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.guiobject.GUIObject;
import com.pipai.wf.guiobject.LeftClickable;
import com.pipai.wf.guiobject.LeftClickable3D;
import com.pipai.wf.guiobject.Renderable;
import com.pipai.wf.guiobject.RightClickable;
import com.pipai.wf.guiobject.battle.AgentGUIObject;
import com.pipai.wf.guiobject.battle.BattleTerrainRenderer;
import com.pipai.wf.guiobject.battle.BulletGUIObject;
import com.pipai.wf.guiobject.battle.FireballGUIObject;
import com.pipai.wf.guiobject.overlay.ActionToolTip;
import com.pipai.wf.guiobject.overlay.AttackButtonOverlay;
import com.pipai.wf.guiobject.overlay.TemporaryText;
import com.pipai.wf.guiobject.overlay.WeaponIndicator;
import com.pipai.wf.util.RayMapper;

/*
 * Simple 2D GUI for rendering a BattleMap
 */

public class BattleGUI extends GUI implements BattleObserver {
	
	public static enum Mode {
		NONE(true),
		TARGET_SELECT(true),
		ANIMATION(false),
		AI(false);
		
		private boolean allowInput;
		
		private Mode(boolean allowInput) {
			this.allowInput = allowInput;
		}
		
		public boolean allowsInput() {
			return allowInput;
		}
	}
    
	private static final int AI_MOVE_WAIT_TIME = 60;

	private Camera camera;
	private OrthographicCamera overlayCamera, orthoCamera;
	private RayMapper rayMapper;
	private BattleController battle;
	private HashMap<Agent, AgentGUIObject> agentMap;
	private ArrayList<AgentGUIObject> agentList;
	private LinkedList<AgentGUIObject> selectableAgentOrderedList, targetAgentList;
	private AgentGUIObject selectedAgent, targetAgent;
	private MapGraph selectedMapGraph;
	private ArrayList<Renderable> renderables, foregroundRenderables, renderablesCreateBuffer, renderablesDelBuffer, overlayRenderables;
	private ArrayList<LeftClickable3D> leftClickables, leftClickablesCreateBuffer, leftClickablesDelBuffer; 
	private ArrayList<LeftClickable> overlayLeftClickables;
	private ArrayList<RightClickable> rightClickables, rightClickablesCreateBuffer, rightClickablesDelBuffer;
	private Mode mode;
	private boolean aiTurn;
	private int aiMoveWait = 0;
	private float cameraMoveTime = 1;
	private Vector3 cameraDest = null;
	private AI ai;
	private BattleTerrainRenderer terrainRenderer;
	private ActionToolTip tooltip;
	private WeaponIndicator weaponIndicator;
	private Attack targetModeAttack;
	private Spell targetModeSpell;
	
	public BattleGUI(WFGame game, BattleMap map) {
		super(game);
		int SQUARE_SIZE = BattleTerrainRenderer.SQUARE_SIZE;
        camera = new PerspectiveCamera(67, this.getScreenWidth(), this.getScreenHeight());
        camera.position.set(0, -200, 400);
        camera.lookAt(0, 0, 0);
        camera.near = 1f;
        camera.far = 2000;
        overlayCamera = new OrthographicCamera();
        orthoCamera = new OrthographicCamera();
        overlayCamera.setToOrtho(false, this.getScreenWidth(), this.getScreenHeight());
        orthoCamera.setToOrtho(false, this.getScreenWidth(), this.getScreenHeight());
        rayMapper = new RayMapper(camera);
		this.battle = new BattleController(map);
		this.battle.registerObserver(this);
		this.ai = new RandomAI(battle);
		this.aiTurn = false;
		this.mode = Mode.NONE;
		this.renderables = new ArrayList<Renderable>();
		this.foregroundRenderables = new ArrayList<Renderable>();
		this.leftClickables = new ArrayList<LeftClickable3D>();
		this.rightClickables = new ArrayList<RightClickable>();
		this.overlayRenderables = new ArrayList<Renderable>();
		this.overlayLeftClickables = new ArrayList<LeftClickable>();
		this.renderablesCreateBuffer = new ArrayList<Renderable>();
		this.leftClickablesCreateBuffer = new ArrayList<LeftClickable3D>();
		this.rightClickablesCreateBuffer = new ArrayList<RightClickable>();
		this.renderablesDelBuffer = new ArrayList<Renderable>();
		this.leftClickablesDelBuffer = new ArrayList<LeftClickable3D>();
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
//			this.leftClickables.add(a);
			this.rightClickables.add(a);
		}
		this.batch.set3DCamera(this.camera);
		this.terrainRenderer = new BattleTerrainRenderer(this, map);
		this.generateOverlays();
		this.setSelected(this.selectableAgentOrderedList.getFirst());
	}
	
	private void generateOverlays() {
		AttackButtonOverlay atkBtn = new AttackButtonOverlay(this);
		this.overlayRenderables.add(atkBtn);
		this.overlayLeftClickables.add(atkBtn);
		this.tooltip = new ActionToolTip(this, 0, 120, 320, 120);
		this.weaponIndicator = new WeaponIndicator(this, this.getScreenWidth() - 120, 80, 120, 80);
		this.overlayRenderables.add(this.tooltip);
		this.overlayRenderables.add(this.weaponIndicator);
	}
	
	private void beginAnimation() { this.mode = Mode.ANIMATION; }
	public void endAnimation() {
		if (aiTurn) {
			this.mode = Mode.AI;
		} else {
			this.mode = Mode.NONE;
			this.populateSelectableAgentList();
			this.performPostInputChecks();
		}
	}
	
	public Mode getMode() { return this.mode; }
	public RayMapper getRayMapper() { return this.rayMapper; }
	
	public void setSelected(AgentGUIObject agent) {
		if (agent.getAgent().getAP() > 0) {
			this.selectedAgent = agent;
			this.weaponIndicator.updateToAgent(agent);
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
		if (o instanceof LeftClickable3D) {
			leftClickablesCreateBuffer.add((LeftClickable3D)o);
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
		if (o instanceof LeftClickable3D) {
			leftClickablesDelBuffer.add((LeftClickable3D)o);
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
		for (LeftClickable3D o : leftClickablesCreateBuffer) {
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
		for (LeftClickable3D o : leftClickablesDelBuffer) {
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
//			vectorized.add(centerOfGridPos(p));
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
		if (this.battle.battleResult() != BattleController.Result.NONE) {
			this.game.setScreen(new BattleResultsGUI(this.game));
			this.dispose();
			return;
		}
		if (this.selectedAgent.getAgent().getAP() == 0 || this.selectedAgent.getAgent().isKO()) {
			this.selectableAgentOrderedList.remove(this.selectedAgent);
			if (this.selectableAgentOrderedList.size() > 0) {
				this.setSelected(this.selectableAgentOrderedList.getFirst());
			}
			this.mode = Mode.NONE;
		}
		for (AgentGUIObject a : this.agentList) {
			if (a.getAgent().getTeam() == Team.PLAYER && (a.getAgent().getAP() > 0 && !a.getAgent().isKO())) {
				return;
			}
		}
		this.mode = Mode.AI;
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
			Action atk = null;
			if (this.targetModeAttack != null) {
				if (selectedAgent.getAgent().getCurrentWeapon().currentAmmo() > 0) {
					atk = new RangeAttackAction(selectedAgent.getAgent(), target.getAgent(), this.targetModeAttack);
				}
			} else {
				atk = new CastTargetAction(selectedAgent.getAgent(), target.getAgent());
			}
			try {
				this.battle.performAction(atk);
			} catch (IllegalActionException e) {
				System.out.println("Illegal move: " + e.getMessage());
			}
		}
		this.updatePaths();
		this.mode = Mode.NONE;
	}
	
	public void onLeftClick(int screenX, int screenY) {
		if (!this.mode.allowsInput()) {
			return;
		}
		Ray ray = camera.getPickRay(screenX, screenY);
		Vector2 gamePos = screenPosToGraphicPos(screenX, screenY);
		int gameX = (int)gamePos.x;
		int gameY = (int)gamePos.y;
		if (this.mode != Mode.ANIMATION) {
			for (LeftClickable o : overlayLeftClickables) {
				o.onLeftClick(screenX, screenY, gameX, gameY);
			}
			for (LeftClickable3D o : leftClickables) {
				o.onLeftClick(ray);
			}
			this.performPostInputChecks();
		}
	}
	
	public void onRightClick(int screenX, int screenY) {
		if (!this.mode.allowsInput()) {
			return;
		}
		Vector2 gamePos = screenPosToGraphicPos(screenX, screenY);
		int gameX = (int)gamePos.x;
		int gameY = (int)gamePos.y;
		boolean performedMove = false;
		if (this.mode != Mode.ANIMATION) {
//			if (this.selectedAgent != null) {
//				GridPosition clickSquare = gamePosToGridPos(gameX, gameY);
//				if (this.selectedMapGraph.canMoveTo(clickSquare)) {
//					LinkedList<GridPosition> path = selectedMapGraph.getPath(clickSquare);
//					MoveAction move = new MoveAction(selectedAgent.getAgent(), path);
//					try {
//						this.battle.performAction(move);
//						performedMove = true;
//					} catch (IllegalActionException e) {
//						System.out.println("IllegalMoveException detected: " + e.getMessage());
//					}
//				}
//			}
			for (RightClickable o : rightClickables) {
				o.onRightClick(gameX, gameY);
			}
			if (!performedMove) {
				this.performPostInputChecks();
			}
		}
	}
	
	public void animateEvent(BattleEvent event) {
		AgentGUIObject a = null, t;
		TemporaryText ttext;
		Vector2 ttextCoord;
		switch (event.getType()) {
		case MOVE:
			a = this.agentMap.get(event.getPerformer());
			beginAnimation();
			a.animateMoveSequence(vectorizePath(event.getPath()), event.getChainEvents());
			this.updatePaths();
			this.moveCameraToPos(a.x, a.y);
			break;
		case ATTACK:
		case OVERWATCH_ACTIVATION:
			a = this.agentMap.get(event.getPerformer());
			t = this.agentMap.get(event.getTarget());
			BulletGUIObject b = new BulletGUIObject(this, a.x, a.y, t.x, t.y, t, event);
			this.createInstance(b);
			this.moveCameraToPos((a.x + t.x)/2, (a.y + t.y)/2);
			break;
		case CAST_TARGET:
			a = this.agentMap.get(event.getPerformer());
			t = this.agentMap.get(event.getTarget());
			FireballGUIObject fireball = new FireballGUIObject(this, a.x, a.y, t.x, t.y, t, event);
			this.createInstance(fireball);
			this.moveCameraToPos((a.x + t.x)/2, (a.y + t.y)/2);
			break;
		case OVERWATCH:
			a = this.agentMap.get(event.getPerformer());
			ttext = new TemporaryText(this, new Vector3(a.x, a.y, 0), 80, 24, "Overwatch");
			this.createInstance(ttext);
			this.moveCameraToPos(a.x, a.y);
			break;
		case RELOAD:
			a = this.agentMap.get(event.getPerformer());
			ttext = new TemporaryText(this, new Vector3(a.x, a.y, 0), 60, 24, "Reload");
			this.createInstance(ttext);
			this.moveCameraToPos(a.x, a.y);
			break;
		case READY:
			a = this.agentMap.get(event.getPerformer());
			ttext = new TemporaryText(this, new Vector3(a.x, a.y, 0), 120, 24, "Ready: " + event.getSpell().name());
			this.createInstance(ttext);
			this.moveCameraToPos(a.x, a.y);
			break;
		case START_TURN:
			if (event.getTeam() == Team.PLAYER) {
				this.mode = Mode.NONE;
				this.aiTurn = false;
				this.populateSelectableAgentList();
				this.setSelected(this.selectableAgentOrderedList.getFirst());
			}
			break;
		default:
			break;
		}
	}
	
	public void switchToTargetMode(Attack atk) {
		this.targetModeSpell = null;
		this.targetModeAttack = atk;
		this.targetAgentList = new LinkedList<AgentGUIObject>();
		for (Agent a : this.selectedAgent.getAgent().enemiesInRange()) {
			this.targetAgentList.add(this.agentMap.get(a));
		}
		this.mode = Mode.TARGET_SELECT;
		if (this.targetAgentList.size() == 0) {
			this.tooltip.setToGeneralDescription(atk.name(), "No enemies in range");
			return;
		}
		this.switchTarget(this.targetAgentList.getFirst());
	}
	
	public void switchToTargetMode(Spell spell) {
		this.targetModeAttack = null;
		this.targetModeSpell = spell;
		this.targetAgentList = new LinkedList<AgentGUIObject>();
		for (Agent a : this.selectedAgent.getAgent().enemiesInRange()) {
			this.targetAgentList.add(this.agentMap.get(a));
		}
		this.mode = Mode.TARGET_SELECT;
		if (this.targetAgentList.size() == 0) {
			this.tooltip.setToGeneralDescription(spell.name(), "No enemies in range");
			return;
		}
		this.switchTarget(this.targetAgentList.getFirst());
	}
	
	public void switchTarget(AgentGUIObject target) {
		if (this.mode == Mode.TARGET_SELECT) {
			if (this.targetAgentList.contains(target)) {
				this.targetAgent = target;
				Weapon weapon = this.selectedAgent.getAgent().getCurrentWeapon();
				if (weapon.needsAmmunition() && weapon.currentAmmo() < this.targetModeAttack.requiredAmmo()) {
					this.tooltip.setToGeneralDescription("Attack", "Not enough ammunition");
				} else {
					if (this.targetModeAttack != null) {
						int acc = this.targetModeAttack.getAccuracy(this.selectedAgent.getAgent(), this.targetAgent.getAgent(), 0);
						int crit = this.targetModeAttack.getCritPercentage(this.selectedAgent.getAgent(), this.targetAgent.getAgent(), 0);
						this.tooltip.setToAttackDescription(this.targetModeAttack, acc, crit);
					} else if (this.targetModeSpell != null) {
						int acc = this.targetModeSpell.getAccuracy(this.selectedAgent.getAgent(), this.targetAgent.getAgent(), 0);
						int crit = this.targetModeSpell.getCritPercentage(this.selectedAgent.getAgent(), this.targetAgent.getAgent(), 0);
						this.tooltip.setToTargetableSpellDescription(this.targetModeSpell, acc, crit);
					}
				}
				this.moveCameraToPos((this.selectedAgent.x + target.x)/2, (this.selectedAgent.y + target.y)/2);
			}
		}
	}
	
	public AgentGUIObject getTarget() {
		return this.targetAgent;
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
		}
		if (this.mode.allowsInput()) {
			if (this.checkKey(Keys.A)) {
				this.cameraMoveTime = 1;
				this.camera.translate(-3, 0, 0);
			}
			if (this.checkKey(Keys.D)) {
				this.cameraMoveTime = 1;
				this.camera.translate(3, 0, 0);
			}
			if (this.checkKey(Keys.W)) {
				this.cameraMoveTime = 1;
				this.camera.translate(0, 3, 0);
			}
			if (this.checkKey(Keys.S)) {
				this.cameraMoveTime = 1;
				this.camera.translate(0, -3, 0);
			}
		}
        this.camera.update();
        this.orthoCamera.update();
	}
	
	@Override
	public void onKeyDown(int keycode) {
		if (keycode == Keys.ESCAPE) {
			if (this.mode != Mode.TARGET_SELECT) {
				Gdx.app.exit();
			} else {
				this.mode = Mode.NONE;
				return;
			}
		}
		if (!this.mode.allowsInput() || selectedAgent == null) {
			return;
		}
		Action action = null;
		switch (keycode) {
		case Keys.NUM_1:
			if (this.mode == Mode.NONE) {
				if (selectedAgent.getAgent().getCurrentWeapon() instanceof SpellWeapon) {
					Spell readiedSpell = ((SpellWeapon)selectedAgent.getAgent().getCurrentWeapon()).getSpell();
					if (readiedSpell != null) {
						this.switchToTargetMode(readiedSpell);
					}
				} else {
					this.switchToTargetMode(new SimpleRangedAttack());
				}
			}
			break;
		case Keys.ENTER:
			if (this.mode == Mode.TARGET_SELECT) {
				this.attack(this.targetAgent);
			}
			break;
		case Keys.X:
			if (this.mode == Mode.NONE) {
				action = new SwitchWeaponAction(selectedAgent.getAgent());
			}
			break;
		case Keys.R:
			// Reload
			if (selectedAgent.getAgent().getCurrentWeapon() instanceof SpellWeapon) {
				action = new ReadySpellAction(selectedAgent.getAgent(), new FireballSpell());
			} else {
				action = new ReloadAction(selectedAgent.getAgent());
			}
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
			if (this.mode == Mode.NONE) {
				this.selectableAgentOrderedList.remove(this.selectedAgent);
				this.selectableAgentOrderedList.addLast(this.selectedAgent);
				this.setSelected(this.selectableAgentOrderedList.peek());
			} else if (this.mode == Mode.TARGET_SELECT) {
				this.targetAgentList.remove(this.targetAgent);
				this.targetAgentList.addLast(this.targetAgent);
				this.switchTarget(this.targetAgentList.peek());
			}
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
		//this.camera.setToOrtho(false, width, height);
		this.orthoCamera.setToOrtho(false, width, height);
		this.overlayCamera.setToOrtho(false, width, height);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		globalUpdate();
        batch.getSpriteBatch().setProjectionMatrix(camera.combined);
        batch.getShapeRenderer().setProjectionMatrix(camera.combined);
        this.terrainRenderer.render(batch);
//		renderShape(batch.getShapeRenderer());
		for (Renderable r : this.renderables) {
			r.render(batch);
		}
		batch.getDecalBatch().flush();
        batch.getSpriteBatch().setProjectionMatrix(orthoCamera.combined);
        batch.getShapeRenderer().setProjectionMatrix(orthoCamera.combined);
		for (Renderable r : this.foregroundRenderables) {
			r.render(batch);
		}
        batch.getSpriteBatch().setProjectionMatrix(overlayCamera.combined);
        batch.getShapeRenderer().setProjectionMatrix(overlayCamera.combined);
		for (Renderable r : this.overlayRenderables) {
			r.render(batch);
		}
		drawFPS();
		if (aiTurn) {
			runAiTurn();
		}
		cleanDelBuffers();
		cleanCreateBuffers();
	}
	
	private void drawFPS() {
		BitmapFont font = batch.getFont();
		batch.getSpriteBatch().begin();
		font.setColor(Color.WHITE);
		font.draw(batch.getSpriteBatch(), String.valueOf(Gdx.graphics.getFramesPerSecond()), this.getScreenWidth() - 24, this.getScreenHeight() - font.getLineHeight()/2);
		batch.getSpriteBatch().end();
	}

}