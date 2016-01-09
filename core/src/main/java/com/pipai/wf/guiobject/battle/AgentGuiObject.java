package com.pipai.wf.guiobject.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.vision.AgentVisionCalculator;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.BattleGui;
import com.pipai.wf.guiobject.GuiObject;
import com.pipai.wf.guiobject.GuiRenderable;
import com.pipai.wf.guiobject.LeftClickable3D;
import com.pipai.wf.guiobject.RightClickable3D;
import com.pipai.wf.guiobject.XYZPositioned;
import com.pipai.wf.guiobject.overlay.AnchoredAgentInfoDisplay;
import com.pipai.wf.util.UtilFunctions;

public class AgentGuiObject extends GuiObject implements XYZPositioned, GuiRenderable, LeftClickable3D, RightClickable3D {

	private BattleGui gui;
	private Agent agent;
	private boolean selected, showingKO;
	private float x, y;
	private int radius;

	private int displayHP, displayArmorHP;

	private Texture circleTex;
	private Decal decal;

	public AgentGuiObject(BattleGui gui, Agent agent) {
		super(gui);
		this.gui = gui;
		this.agent = agent;
		displayHP = agent.getHP();
		displayArmorHP = agent.getInventory().isEquippingArmor() ? agent.getEquippedArmor().getHP() : 0;
		selected = false;
		int squareSize = BattleTerrainRenderer.SQUARE_SIZE;
		Vector2 xy = BattleTerrainRenderer.centerOfGridPos(agent.getPosition());
		x = xy.x;
		y = xy.y;
		radius = squareSize / 2;
		showingKO = false;
		Pixmap pixmap = new Pixmap(squareSize, squareSize, Pixmap.Format.RGBA8888);
		pixmap.fill();
		pixmap.setColor(Color.RED);
		pixmap.fillCircle(squareSize / 2, squareSize / 2, squareSize / 2 - 1);
		circleTex = new Texture(pixmap);
		pixmap.dispose();
		decal = Decal.newDecal(new TextureRegion(circleTex), true);
		decal.setDimensions(32, 32);
		gui.createInstance(new AnchoredAgentInfoDisplay(gui, this));
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getZ() {
		return 16;
	}

	@Override
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	@Override
	public void setZ(float z) {
		// Do nothing for now
	}

	@Override
	public Vector3 getPosition() {
		return new Vector3(x, y, 0);
	}

	public GridPosition getDisplayPosition() {
		return BattleTerrainRenderer.gamePosToGridPos(x, y);
	}

	@Override
	public int renderPriority() {
		return 0;
	}

	public Agent getAgent() {
		return agent;
	}

	public void select() {
		selected = true;
		gui.setSelected(this);
	}

	public void deselect() {
		selected = false;
	}

	public boolean isSelected() {
		return selected;
	}

	/**
	 * @return If displaying as KOed, not necessarily the Agent
	 */
	public boolean isShowingKO() {
		return showingKO;
	}

	public int getDisplayHP() {
		return displayHP;
	}

	public int getDisplayArmorHP() {
		return displayArmorHP;
	}

	public void hit(BattleEvent outcome) {
		int dmg = outcome.getDamage();
		displayArmorHP -= dmg;
		if (displayArmorHP < 0) {
			// Add the negative damage
			displayHP += displayArmorHP;
			displayArmorHP = 0;
		}
		if (displayHP <= 0) {
			displayHP = 0;
			showingKO = true;
		}
	}

	@Override
	public void update() {
		decal.setPosition(x, y, getZ());
		decal.setRotation(gui.getCamera().getCamera().direction, gui.getCamera().getCamera().up);
	}

	public boolean visible() {
		if (isShowingKO()) {
			return false;
		}
		if (agent.getTeam() != Team.PLAYER) {
			AgentVisionCalculator agentVisionCalculator = new AgentVisionCalculator(gui.getBattleController().getBattleMap(),
					gui.getBattleController().getBattleConfiguration());
			if (agentVisionCalculator.enemiesInRangeOf(agent).size() == 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void render(BatchHelper batch) {
		if (visible()) {
			batch.getDecalBatch().add(decal);
		}
	}

	public void onLeftClick(int screenX, int screenY, int gameX, int gameY) {
		if (UtilFunctions.isInCircle(x, y, radius, gameX, gameY)) {
			if (agent.getTeam() == Team.PLAYER) {
				select();
			} else {
				gui.switchTarget(this);
			}
		}
	}

	@Override
	public boolean onLeftClick(Ray ray) {
		// ray.origin + t * ray.direction = 0
		float t = -ray.origin.z / ray.direction.z;
		Vector3 endpoint = new Vector3();
		ray.getEndPoint(endpoint, t);
		if (UtilFunctions.isInCircle(x, y, radius, endpoint.x, endpoint.y)) {
			if (agent.getTeam() == Team.PLAYER) {
				select();
			} else {
				gui.switchTarget(this);
			}
		}
		return true;
	}

	@Override
	public boolean onRightClick(Ray ray) {
		float t = -ray.origin.z / ray.direction.z;
		Vector3 endpoint = new Vector3();
		ray.getEndPoint(endpoint, t);
		if (UtilFunctions.isInCircle(x, y, radius, endpoint.x, endpoint.y)) {
			if (gui.getMode() == BattleGui.Mode.TARGET_SELECT && this == gui.getTargetAgent()) {
				gui.performAttackAction(this);
			}
		}
		return true;
	}

}
