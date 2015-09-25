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
	private boolean selected, ko;
	public float x, y;
	public int radius;

	public int displayHP, displayArmorHP;

	private Texture circleTex;
	private Decal decal;

	public AgentGuiObject(BattleGui gui, Agent agent) {
		super(gui);
		this.gui = gui;
		this.agent = agent;
		displayHP = agent.getHP();
		displayArmorHP = agent.getArmor().getHP();
		this.selected = false;
		int SQUARE_SIZE = BattleTerrainRenderer.SQUARE_SIZE;
		Vector2 xy = BattleTerrainRenderer.centerOfGridPos(agent.getPosition());
		this.x = xy.x;
		this.y = xy.y;
		radius = SQUARE_SIZE / 2;
		ko = false;
		Pixmap pixmap = new Pixmap(SQUARE_SIZE, SQUARE_SIZE, Pixmap.Format.RGBA8888);
		pixmap.fill();
		pixmap.setColor(Color.RED);
		pixmap.fillCircle(SQUARE_SIZE / 2, SQUARE_SIZE / 2, SQUARE_SIZE / 2 - 1);
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
		return 0;
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
		return;
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
		return this.agent;
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
		return ko;
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
			displayHP += displayArmorHP;	// Add the negative damage
			displayArmorHP = 0;
		}
		if (displayHP <= 0) {
			displayHP = 0;
			ko = true;
		}
	}

	@Override
	public void update() {
		decal.setPosition(x, y, 0);
		decal.setRotation(gui.getCamera().getCamera().direction, gui.getCamera().getCamera().up);
	}

	public boolean visible() {
		if (isShowingKO()) {
			return false;
		}
		if (agent.getTeam() != Team.PLAYER) {
			if (agent.enemiesInRange().size() == 0) {
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
			if (this.agent.getTeam() == Team.PLAYER) {
				this.select();
			} else {
				this.gui.switchTarget(this);
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
			if (this.agent.getTeam() == Team.PLAYER) {
				this.select();
			} else {
				this.gui.switchTarget(this);
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
			if (this.gui.getMode() == BattleGui.Mode.TARGET_SELECT && this == this.gui.getTarget()) {
				this.gui.performAttackAction(this);
			}
		}
		return true;
	}

}