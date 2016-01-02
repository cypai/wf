package com.pipai.wf.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.Ray;
import com.pipai.wf.WFGame;
import com.pipai.wf.battle.Battle;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.BattleObserver;
import com.pipai.wf.battle.BattleResult;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.action.ReadySpellAction;
import com.pipai.wf.battle.action.ReloadAction;
import com.pipai.wf.battle.action.SwitchWeaponAction;
import com.pipai.wf.battle.action.TargetedAction;
import com.pipai.wf.battle.action.TargetedActionable;
import com.pipai.wf.battle.action.WeaponActionFactory;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.ai.AI;
import com.pipai.wf.battle.ai.AIMoveRunnable;
import com.pipai.wf.battle.ai.TopModularAI;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapGraph;
import com.pipai.wf.battle.spell.FireballSpell;
import com.pipai.wf.battle.vision.AgentVisionCalculator;
import com.pipai.wf.battle.vision.FogOfWar;
import com.pipai.wf.battle.weapon.SpellWeapon;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.gui.animation.AnimationController;
import com.pipai.wf.gui.animation.AnimationControllerObserver;
import com.pipai.wf.gui.animation.BulletAttackAnimationHandler;
import com.pipai.wf.gui.animation.CastTargetAnimationHandler;
import com.pipai.wf.gui.animation.MoveAnimationHandler;
import com.pipai.wf.gui.animation.OverwatchAnimationHandler;
import com.pipai.wf.gui.animation.ReadySpellAnimationHandler;
import com.pipai.wf.gui.animation.ReloadAnimationHandler;
import com.pipai.wf.gui.animation.SuppressionAnimationHandler;
import com.pipai.wf.gui.camera.AnchoredCamera;
import com.pipai.wf.gui.util.GuiObjectBuffers;
import com.pipai.wf.guiobject.GuiObject;
import com.pipai.wf.guiobject.GuiRenderable;
import com.pipai.wf.guiobject.LeftClickable;
import com.pipai.wf.guiobject.LeftClickable3D;
import com.pipai.wf.guiobject.RightClickable3D;
import com.pipai.wf.guiobject.battle.AgentGuiObject;
import com.pipai.wf.guiobject.battle.BattleTerrainRenderer;
import com.pipai.wf.guiobject.overlay.ActionToolTip;
import com.pipai.wf.guiobject.overlay.AgentStatusWindow;
import com.pipai.wf.guiobject.overlay.WeaponIndicator;
import com.pipai.wf.unit.ability.Ability;
import com.pipai.wf.unit.ability.PrecisionShotAbility;
import com.pipai.wf.unit.ability.SuppressionAbility;
import com.pipai.wf.unit.ability.component.TargetedAbilityComponent;
import com.pipai.wf.util.RayMapper;

/*
 * Simple 2D GUI for rendering a BattleMap
 */

public final class BattleGui extends Gui implements BattleObserver, AnimationControllerObserver {

	private static final Logger logger = LoggerFactory.getLogger(BattleGui.class);

	public enum Mode {
		MOVE(true), TARGET_SELECT(true), PRE_ANIMATION(false), ANIMATION(false), AI(false);

		private boolean allowInput;

		Mode(boolean allowInput) {
			this.allowInput = allowInput;
		}

		public boolean allowsInput() {
			return allowInput;
		}
	}

	private static final int AI_MOVE_WAIT_TIME = 60;

	private AnchoredCamera camera;
	private OrthographicCamera overlayCamera, orthoCamera;
	private RayMapper rayMapper;
	private BattleController battleController;
	private HashMap<Agent, AgentGuiObject> agentMap;
	private ArrayList<AgentGuiObject> agentList;
	private LinkedList<AgentGuiObject> selectableAgentOrderedList, targetAgentList;
	private AgentGuiObject selectedAgent, targetAgent;
	private MapGraph selectedMapGraph;
	private ArrayList<GuiRenderable> renderables, foregroundRenderables, renderablesCreateBuffer, renderablesDelBuffer, overlayRenderables;
	private GuiObjectBuffers<LeftClickable3D> leftClickables;
	private GuiObjectBuffers<RightClickable3D> rightClickables;
	private ArrayList<LeftClickable> overlayLeftClickables;
	private Mode mode;
	private boolean aiTurn;
	private int aiMoveWait;
	private AI ai;
	private BattleTerrainRenderer terrainRenderer;
	private ActionToolTip tooltip;
	private WeaponIndicator weaponIndicator;
	private AgentStatusWindow agentStatusWindow;
	private TargetedAction targetedAction;
	private AnimationController animationController;
	private FogOfWar fogOfWar;

	private boolean switchGuiFlag;
	private BattleResult switchBattleResultGuiData;

	public BattleGui(WFGame game, Battle battle) {
		super(game);
		camera = new AnchoredCamera(getScreenWidth(), getScreenHeight());
		overlayCamera = new OrthographicCamera();
		orthoCamera = new OrthographicCamera();
		overlayCamera.setToOrtho(false, getScreenWidth(), getScreenHeight());
		orthoCamera.setToOrtho(false, getScreenWidth(), getScreenHeight());
		rayMapper = new RayMapper(camera.getCamera());
		battleController = battle.getBattleController();
		battleController.registerObserver(this);
		ai = new TopModularAI(battleController);
		aiTurn = false;
		mode = Mode.MOVE;
		renderables = new ArrayList<GuiRenderable>();
		foregroundRenderables = new ArrayList<GuiRenderable>();
		renderablesCreateBuffer = new ArrayList<GuiRenderable>();
		renderablesDelBuffer = new ArrayList<GuiRenderable>();
		leftClickables = new GuiObjectBuffers<LeftClickable3D>();
		rightClickables = new GuiObjectBuffers<RightClickable3D>();
		overlayRenderables = new ArrayList<GuiRenderable>();
		overlayLeftClickables = new ArrayList<LeftClickable>();
		agentMap = new HashMap<Agent, AgentGuiObject>();
		agentList = new ArrayList<AgentGuiObject>();
		selectableAgentOrderedList = new LinkedList<AgentGuiObject>();
		for (Agent agent : battleController.getBattleMap().getAgents()) {
			AgentGuiObject a = new AgentGuiObject(this, agent);
			agentMap.put(agent, a);
			agentList.add(a);
			if (agent.getTeam() == Team.PLAYER) {
				selectableAgentOrderedList.add(a);
			}
			createInstance(a);
		}
		getBatch().set3DCamera(camera.getCamera());
		fogOfWar = new FogOfWar(battle.getBattleMap(), selectableAgentOrderedList);
		// this.fogOfWar.fullScan();
		terrainRenderer = new BattleTerrainRenderer(this, battle.getBattleMap(), fogOfWar);
		createInstance(terrainRenderer);
		generateOverlays();
		animationController = new AnimationController(this);
		setSelected(selectableAgentOrderedList.getFirst());
	}

	private void generateOverlays() {
		tooltip = new ActionToolTip(this, 0, 120, 320, 120);
		weaponIndicator = new WeaponIndicator(this, getScreenWidth() - 120, 80, 120, 80);
		agentStatusWindow = new AgentStatusWindow(this);
		overlayRenderables.add(tooltip);
		overlayRenderables.add(weaponIndicator);
		overlayRenderables.add(agentStatusWindow);
	}

	private void beginAnimation() {
		mode = Mode.ANIMATION;
	}

	public void endAnimation() {
		performVictoryCheck();
		if (aiTurn) {
			mode = Mode.AI;
			aiMoveWait = 0;
		} else {
			fogOfWar.fullScan();
			populateSelectableAgentList();
			mode = Mode.MOVE;
			// Check if we need to change selected Agent
			if (selectedAgent.getAgent().getAP() == 0 || selectedAgent.getAgent().isKO()) {
				selectableAgentOrderedList.remove(selectedAgent);
				if (selectableAgentOrderedList.size() > 0) {
					setSelected(selectableAgentOrderedList.getFirst());
				}
			}
			for (AgentGuiObject a : agentList) {
				if (a.getAgent().getTeam() == Team.PLAYER && (a.getAgent().getAP() > 0 && !a.getAgent().isKO())) {
					// Found a movable Agent, so we return and do not start AI turn
					updatePaths();
					terrainRenderer.setMovingTiles(selectedMapGraph);
					return;
				}
			}
			// All moves finished - start the AI
			startAiTurn();
		}
	}

	public Mode getMode() {
		return mode;
	}

	public RayMapper getRayMapper() {
		return rayMapper;
	}

	public AnchoredCamera getCamera() {
		return camera;
	}

	public void setSelected(AgentGuiObject agent) {
		if (agent.getAgent().getAP() > 0) {
			selectedAgent = agent;
			weaponIndicator.updateToAgent(agent);
			moveCameraToPos(selectedAgent.getX(), selectedAgent.getY());
			updatePaths();
			terrainRenderer.setMovingTiles(selectedMapGraph);
		}
	}

	@Override
	public void createInstance(GuiObject o) {
		super.createInstance(o);
		if (o instanceof GuiRenderable) {
			renderablesCreateBuffer.add((GuiRenderable) o);
		}
		if (o instanceof LeftClickable3D) {
			leftClickables.addToCreateBuffer((LeftClickable3D) o);
		}
		if (o instanceof RightClickable3D) {
			rightClickables.addToCreateBuffer((RightClickable3D) o);
		}
	}

	@Override
	public void deleteInstance(GuiObject o) {
		super.deleteInstance(o);
		if (o instanceof GuiRenderable) {
			renderablesDelBuffer.add((GuiRenderable) o);
		}
		if (o instanceof LeftClickable3D) {
			leftClickables.addToDeleteBuffer((LeftClickable3D) o);
		}
		if (o instanceof RightClickable3D) {
			rightClickables.addToDeleteBuffer((RightClickable3D) o);
		}
	}

	private void cleanCreateBuffers() {
		for (GuiRenderable o : renderablesCreateBuffer) {
			if (o.renderPriority() == -1) {
				foregroundRenderables.add(o);
			} else {
				renderables.add(o);
			}
		}
		renderablesCreateBuffer.clear();
		leftClickables.flushCreateBuffer();
		rightClickables.flushCreateBuffer();
	}

	private void cleanDelBuffers() {
		for (GuiRenderable o : renderablesDelBuffer) {
			if (!renderables.remove(o)) {
				foregroundRenderables.remove(o);
			}
		}
		renderablesDelBuffer.clear();
		leftClickables.flushDeleteBuffer();
		rightClickables.flushDeleteBuffer();
	}

	public void updatePaths() {
		if (selectedAgent != null) {
			Agent a = selectedAgent.getAgent();
			MapGraph graph = new MapGraph(battleController.getBattleMap(), a.getPosition(), a.getEffectiveMobility(), a.getAP(), a.getMaxAP());
			selectedMapGraph = graph;
		}
	}

	private Vector2 screenPosToGraphicPos(int screenX, int screenY) {
		float x = camera.position().x - getScreenWidth() / 2 + screenX;
		float y = camera.position().y - getScreenHeight() / 2 + screenY;
		return new Vector2(x, y);
	}

	@Override
	public void notifyBattleEvent(BattleEvent ev) {
		animateEvent(ev);
	}

	private void performVictoryCheck() {
		BattleResult result = battleController.battleResult();
		if (result.getResult() != BattleResult.Result.NONE) {
			switchGuiFlag = true;
			switchBattleResultGuiData = result;
		}
	}

	private void performGuiSwitchIfFlagSet() {
		if (switchGuiFlag) {
			switchGui(new BattleResultsGui(getGame(), switchBattleResultGuiData));
		}
	}

	private void startAiTurn() {
		terrainRenderer.clearShadedTiles();
		mode = Mode.AI;
		aiTurn = true;
		aiMoveWait = 0;
		battleController.endTurn();
		ai.startTurn();
	}

	private void runAiTurn() {
		aiMoveWait += 1;
		if (aiMoveWait == BattleGui.AI_MOVE_WAIT_TIME) {
			AIMoveRunnable t = new AIMoveRunnable(ai);
			t.run();
		}
	}

	private void populateSelectableAgentList() {
		selectableAgentOrderedList.clear();
		for (Agent agent : battleController.getBattleMap().getAgents()) {
			if (agent.getTeam() == Team.PLAYER && agent.getAP() > 0 && !agent.isKO()) {
				selectableAgentOrderedList.add(agentMap.get(agent));
			}
		}
	}

	public void performAttackAction(AgentGuiObject target) {
		if (selectedAgent != null) {
			Action atk = null;
			Weapon weapon = selectedAgent.getAgent().getCurrentWeapon();
			if (weapon instanceof SpellWeapon) {
				SpellWeapon sw = (SpellWeapon) weapon;
				atk = sw.getSpell().getAction(battleController, selectedAgent.getAgent(), target.getAgent());
			} else {
				if (weapon.currentAmmo() > 0) {
					if (targetedAction == null) {
						atk = ((TargetedActionable) weapon).getAction(battleController, selectedAgent.getAgent(), target.getAgent());
					} else {
						atk = targetedAction;
					}
				} else {
					return;
				}
			}
			try {
				atk.perform();
			} catch (IllegalActionException e) {
				logger.error("Illegal move: " + e.getMessage());
			}
		}
	}

	public void performMoveWithSelectedAgent(GridPosition destination) {
		if (selectedAgent != null) {
			if (selectedMapGraph.canMoveTo(destination)) {
				LinkedList<GridPosition> path = selectedMapGraph.getPath(destination);
				int useAP = selectedMapGraph.apRequiredToMoveTo(destination);
				MoveAction move = new MoveAction(battleController, selectedAgent.getAgent(), path, useAP);
				terrainRenderer.clearShadedTiles();
				try {
					move.perform();
				} catch (IllegalActionException e) {
					logger.error("IllegalMoveException detected: " + e.getMessage());
				}
			}
		}
	}

	@Override
	public void onLeftClick(int screenX, int screenY) {
		if (!mode.allowsInput()) {
			return;
		}
		Ray ray = rayMapper.screenToRay(screenX, screenY);
		Vector2 gamePos = screenPosToGraphicPos(screenX, screenY);
		int gameX = (int) gamePos.x;
		int gameY = (int) gamePos.y;
		if (mode != Mode.ANIMATION) {
			for (LeftClickable o : overlayLeftClickables) {
				o.onLeftClick(gameX, gameY);
			}
			for (LeftClickable3D o : leftClickables.getGuiObjects()) {
				o.onLeftClick(ray);
			}
		}
	}

	@Override
	public void onRightClick(int screenX, int screenY) {
		if (!mode.allowsInput()) {
			return;
		}
		Ray ray = rayMapper.screenToRay(screenX, screenY);
		if (mode != Mode.ANIMATION) {
			for (RightClickable3D o : rightClickables.getGuiObjects()) {
				o.onRightClick(ray);
			}
		}
	}

	@Override
	public void mouseScrolled(int amount) {
		switch (amount) {
		case 1:
			camera.decreaseHeight();
			break;
		case -1:
			camera.increaseHeight();
			break;
		default:
			throw new IllegalArgumentException("Parameter must 1 or -1, but received: " + amount);
		}
	}

	public void animateEvent(BattleEvent event) {
		if (event.getPerformer() != null && !agentMap.get(event.getPerformer()).visible()) {
			endAnimation();
			return;
		}
		boolean animating = true;
		switch (event.getType()) {
		case MOVE:
			animationController.startAnimation(new MoveAnimationHandler(this, animationController, event, event.getPerformer().getTeam() == Team.PLAYER));
			break;
		case ATTACK:
		case RANGED_WEAPON_ATTACK:
			animationController.startAnimation(new BulletAttackAnimationHandler(this, event));
			break;
		case CAST_TARGET:
			animationController.startAnimation(new CastTargetAnimationHandler(this, event));
			break;
		case OVERWATCH:
			animationController.startAnimation(new OverwatchAnimationHandler(this, event, event.getPerformer().getTeam() == Team.PLAYER));
			break;
		case RELOAD:
			animationController.startAnimation(new ReloadAnimationHandler(this, event, event.getPerformer().getTeam() == Team.PLAYER));
			break;
		case READY:
			animationController.startAnimation(new ReadySpellAnimationHandler(this, event, event.getPerformer().getTeam() == Team.PLAYER));
			break;
		case TARGETED_ACTION:
			animationController.startAnimation(new SuppressionAnimationHandler(this, event));
			break;
		case START_TURN:
			if (event.getTeam() == Team.PLAYER) {
				mode = Mode.MOVE;
				aiTurn = false;
				populateSelectableAgentList();
				setSelected(selectableAgentOrderedList.getFirst());
			}
			animating = false;
			break;
		default:
			animating = false;
			break;
		}
		if (animating) {
			beginAnimation();
		} else {
			// No animation yet, just end now
			endAnimation();
		}
	}

	@Override
	public void notifyControlReleased() {
		endAnimation();
	}

	public void switchToMoveMode() {
		mode = Mode.MOVE;
		terrainRenderer.setMovingTiles(selectedMapGraph);
		moveCameraToPos(selectedAgent.getX(), selectedAgent.getY());
	}

	public void switchToTargetMode() {
		switchToTargetMode(null);
	}

	public void switchToTargetMode(Ability ability) {
		if (ability != null && !(ability instanceof TargetedAbilityComponent)) {
			throw new IllegalArgumentException(ability.getName() + " cannot target anything");
		}
		terrainRenderer.clearShadedTiles();
		targetAgentList = new LinkedList<AgentGuiObject>();
		AgentVisionCalculator agentVisionCalculator = new AgentVisionCalculator(battleController.getBattleMap(), battleController.getBattleConfiguration());
		for (Agent a : agentVisionCalculator.enemiesInRangeOf(selectedAgent.getAgent())) {
			targetAgentList.add(agentMap.get(a));
		}
		mode = Mode.TARGET_SELECT;
		if (targetAgentList.size() == 0) {
			targetedAction = null;
			if (ability == null) {
				tooltip.setToGeneralDescription(new WeaponActionFactory(battleController).defaultWeaponActionName(selectedAgent.getAgent()), "No enemies in range");
			} else {
				tooltip.setToGeneralDescription(ability.getName(), "No enemies in range");
			}
			return;
		}
		this.switchTarget(targetAgentList.getFirst(), (TargetedAbilityComponent) ability);
	}

	public void switchTarget(AgentGuiObject target) {
		switchTarget(target, null);
	}

	public void switchTarget(AgentGuiObject target, TargetedAbilityComponent ability) {
		if (mode == Mode.TARGET_SELECT) {
			if (targetAgentList.contains(target)) {
				ArrayList<GridPosition> targetTiles = new ArrayList<>();
				ArrayList<GridPosition> targetableTiles = new ArrayList<>();
				targetTiles.add(target.getAgent().getPosition());
				for (AgentGuiObject a : targetAgentList) {
					targetableTiles.add(a.getAgent().getPosition());
				}
				terrainRenderer.setTargetableTiles(targetableTiles);
				terrainRenderer.setTargetTiles(targetTiles);
				targetAgent = target;
				if (ability == null) {
					targetedAction = new WeaponActionFactory(battleController).defaultWeaponAction(selectedAgent.getAgent(), target.getAgent());
				} else {
					targetedAction = ability.getTargetedAction(battleController, selectedAgent.getAgent(), target.getAgent());
				}
				tooltip.setToActionDescription(targetedAction);
				moveCameraToPos((selectedAgent.getX() + target.getX()) / 2, (selectedAgent.getY() + target.getY()) / 2);
			}
		}
	}

	public AgentGuiObject getTargetAgent() {
		return targetAgent;
	}

	public AgentGuiObject getAgentGUIObject(Agent a) {
		return agentMap.get(a);
	}

	public BattleController getBattleController() {
		return battleController;
	}

	private void globalUpdate() {
		updateCamera();
	}

	private void moveCameraToPos(float x, float y) {
		camera.moveTo(x, y);
	}

	private void updateCamera() {
		if (mode.allowsInput()) {
			if (checkKey(Keys.A)) {
				camera.moveLeft();
			}
			if (checkKey(Keys.D)) {
				camera.moveRight();
			}
			if (checkKey(Keys.W)) {
				camera.moveUp();
			}
			if (checkKey(Keys.S)) {
				camera.moveDown();
			}
		}
		camera.update();
		orthoCamera.update();
	}

	@Override
	public void onKeyDown(int keycode) {
		if (keycode == Keys.ESCAPE) {
			if (agentStatusWindow.getVisible()) {
				agentStatusWindow.setVisible(false);
				return;
			}
			if (mode == Mode.TARGET_SELECT) {
				switchToMoveMode();
				return;
			} else {
				Gdx.app.exit();
			}
		}
		if (selectedAgent == null || !mode.allowsInput()) {
			return;
		}
		Action action = null;
		switch (keycode) {
		case Keys.NUM_1:
			if (mode == Mode.MOVE) {
				switchToTargetMode();
			}
			break;
		case Keys.ENTER:
			if (mode == Mode.TARGET_SELECT) {
				performAttackAction(targetAgent);
			}
			break;
		case Keys.X:
			if (mode == Mode.MOVE) {
				action = new SwitchWeaponAction(battleController, selectedAgent.getAgent());
			}
			break;
		case Keys.R:
			// Reload
			if (selectedAgent.getAgent().getCurrentWeapon() instanceof SpellWeapon) {
				action = new ReadySpellAction(battleController, selectedAgent.getAgent(), new FireballSpell());
			} else {
				action = new ReloadAction(battleController, selectedAgent.getAgent());
			}
			break;
		case Keys.Y:
			// Overwatch
			action = new OverwatchAction(battleController, selectedAgent.getAgent());
			break;
		case Keys.H:
			// Hunker
			break;
		case Keys.K:
			// Skill
			if (mode == Mode.MOVE) {
				for (Ability a : selectedAgent.getAgent().getAbilities()) {
					if (a instanceof PrecisionShotAbility && !((PrecisionShotAbility) a).onCooldown()) {
						this.switchToTargetMode(a);
						break;
					} else if (a instanceof SuppressionAbility) {
						this.switchToTargetMode(a);
						break;
					}
				}
			}
			break;
		case Keys.M:
			// Recharge mana
			break;
		case Keys.SHIFT_LEFT:
			// Select next unit
			if (mode == Mode.MOVE) {
				selectableAgentOrderedList.remove(selectedAgent);
				selectableAgentOrderedList.addLast(selectedAgent);
				setSelected(selectableAgentOrderedList.peek());
			} else if (mode == Mode.TARGET_SELECT) {
				targetAgentList.remove(targetAgent);
				targetAgentList.addLast(targetAgent);
				this.switchTarget(targetAgentList.peek());
			}
			break;
		case Keys.Q:
			camera.arcballRotationCW();
			break;
		case Keys.E:
			camera.arcballRotationCCW();
			break;
		case Keys.F1:
			if (mode == Mode.MOVE || targetedAction == null) {
				agentStatusWindow.setAgentStatus(selectedAgent.getAgent());
			} else if (mode == Mode.TARGET_SELECT) {
				// TODO: Check out dead code here:
				if (targetedAction instanceof TargetedAction) {
					agentStatusWindow.setTargetedAction(targetedAction);
				} else {
					agentStatusWindow.setAgentStatus(targetedAction.getTarget());
				}
			}
			agentStatusWindow.setVisible(true);
			break;
		case Keys.BACKSLASH:
			terrainRenderer.setRenderingTextures(!terrainRenderer.isRenderingTextures());
			break;
		default:
			break;
		}
		if (action != null) {
			try {
				mode = Mode.PRE_ANIMATION;
				action.perform();
			} catch (IllegalActionException e) {
				logger.error("Illegal move: " + e.getMessage());
				mode = Mode.MOVE;
			}
		}
	}

	@Override
	public void onKeyUp(int keycode) {

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		// this.camera.setToOrtho(false, width, height);
		camera.setViewport(width, height);
		orthoCamera.setToOrtho(false, width, height);
		overlayCamera.setToOrtho(false, width, height);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		animationController.update();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		BatchHelper batch = getBatch();
		batch.getSpriteBatch().setProjectionMatrix(camera.getProjectionMatrix());
		batch.getShapeRenderer().setProjectionMatrix(camera.getProjectionMatrix());
		terrainRenderer.render(batch);
		// renderShape(batch.getShapeRenderer());
		for (GuiRenderable r : renderables) {
			r.render(batch);
		}
		batch.getDecalBatch().flush();
		batch.getSpriteBatch().setProjectionMatrix(orthoCamera.combined);
		batch.getShapeRenderer().setProjectionMatrix(orthoCamera.combined);
		for (GuiRenderable r : foregroundRenderables) {
			r.render(batch);
		}
		batch.getSpriteBatch().setProjectionMatrix(overlayCamera.combined);
		batch.getShapeRenderer().setProjectionMatrix(overlayCamera.combined);
		for (GuiRenderable r : overlayRenderables) {
			r.render(batch);
		}
		// Uncomment to get fog of war texture in bottom left corner
		// batch.getSpriteBatch().begin();
		// batch.getSpriteBatch().draw(fogOfWar.getFogOfWarTexture(), 0, 0);
		// batch.getSpriteBatch().end();
		drawFPS();
		if (aiTurn) {
			runAiTurn();
		}
		cleanDelBuffers();
		cleanCreateBuffers();
		globalUpdate();
		performGuiSwitchIfFlagSet();
	}

	private void drawFPS() {
		BitmapFont font = getBatch().getFont();
		getBatch().getSpriteBatch().begin();
		font.setColor(Color.WHITE);
		font.draw(getBatch().getSpriteBatch(), String.valueOf(Gdx.graphics.getFramesPerSecond()), getScreenWidth() - 24, getScreenHeight() - font.getLineHeight() / 2);
		getBatch().getSpriteBatch().end();
	}

}
