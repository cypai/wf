package com.pipai.wf.guiobject.battle;

import java.util.LinkedList;

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
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.BattleGUI;
import com.pipai.wf.guiobject.GUIObject;
import com.pipai.wf.guiobject.LeftClickable;
import com.pipai.wf.guiobject.LeftClickable3D;
import com.pipai.wf.guiobject.Renderable;
import com.pipai.wf.guiobject.RightClickable;
import com.pipai.wf.guiobject.overlay.AnchoredAgentInfoDisplay;
import com.pipai.wf.util.Alarm;
import com.pipai.wf.util.UtilFunctions;

public class AgentGUIObject extends GUIObject implements Renderable, LeftClickable3D, RightClickable {
	
	private BattleGUI gui;
	private Agent agent;
	private boolean selected, ko;
	public float x, y;
	public int radius;
	
	//Animation variables
	private boolean animating, wait;
	private LinkedList<Vector2> moveSeq;
	private Vector2 start, dest;
	private int t;	//Animation time t counter
	private LinkedList<BattleEvent> chain;
	private Alarm owAlarm;
	
	private Texture circleTex;
	private Decal decal;
	
	public AgentGUIObject(BattleGUI gui, Agent agent, float x, float y, int radius) {
		super(gui);
		this.gui = gui;
		this.agent = agent;
		this.selected = false;
		this.x = x;
		this.y = y;
		this.radius = radius;
		animating = false;
		wait = false;
		ko = false;
		owAlarm = new Alarm();
		int SQUARE_SIZE = BattleTerrainRenderer.SQUARE_SIZE;
		Pixmap pixmap = new Pixmap(SQUARE_SIZE, SQUARE_SIZE, Pixmap.Format.RGBA8888);
		pixmap.fill();
		pixmap.setColor(Color.RED);
		pixmap.fillCircle(SQUARE_SIZE/2, SQUARE_SIZE/2, SQUARE_SIZE/2-1);
		circleTex = new Texture(pixmap);
		pixmap.dispose();
		decal = Decal.newDecal(new TextureRegion(circleTex), true);
		decal.setDimensions(32, 32);
		gui.createInstance(new AnchoredAgentInfoDisplay(gui, this));
	}

	public int renderPriority() { return 0; }
	
	public Agent getAgent() { return this.agent; }
	
	public void select() {
		selected = true; 
		gui.setSelected(this);
	}
	public void deselect() { selected = false; }
	public boolean isSelected() { return selected; }
	
	public void hit() {
		if (this.agent.isKO()) {
			ko = true;
			if (animating) {
				gui.endAnimation();
			}
		}
	}
	
	public void animateMoveSequence(LinkedList<Vector2> seq, LinkedList<BattleEvent> chain) {
		animating = true;
		moveSeq = seq;
		this.chain = chain;
		animateNextMoveInSeq();
	}
	
	private boolean checkOverwatchActivation() {
		LinkedList<BattleEvent> removeBuffer = new LinkedList<BattleEvent>();
		boolean owActive = false;
		if (chain.size() > 0) {
			for (BattleEvent ev : chain) {
				Vector2 owtile = BattleTerrainRenderer.centerOfGridPos(ev.getTargetTile());
				if (owtile.epsilonEquals(start, 0.0001f)) {
					gui.animateEvent(ev);
					removeBuffer.add(ev);
					owActive = true;
				}
			}
			chain.removeAll(removeBuffer);
		}
		return owActive;
	}
	
	private void animateNextMoveInSeq() {
		start = moveSeq.pollFirst();
		dest = moveSeq.peekFirst();
		t = 0;
		if (dest == null) {
			animating = false;
			start = null;
			gui.endAnimation();
		}
	}

	public void update() {
		decal.setPosition(x, y, 0);
		if (!wait) {
			if (animating) {
				t += 1;
				int time = 6;
				if (t <= time) {
					float alpha = (float)t/(float)time;
					x = start.x*(1-alpha) + dest.x*(alpha);
					y = start.y*(1-alpha) + dest.y*(alpha);
				}
				if (t > time) {
					if (checkOverwatchActivation()) {
						wait = true;
						owAlarm.set(60);
						return;
					}
					animateNextMoveInSeq();
				}
			}
		} else {
			owAlarm.update();
			if (owAlarm.check()) {
				wait = false;
			}
		}
	}

	public void render(BatchHelper batch) {
		update();
		if (!ko) {
			batch.getDecalBatch().add(decal);
//			r.begin(ShapeType.Filled);
//			if (agent.getTeam() == Team.PLAYER) {
//				r.setColor(0, 0.8f, 0, 1);
//			} else {
//				r.setColor(0.8f, 0, 0, 1);
//			}
//			r.circle(x, y, radius);
//			r.end();
//			if (this.selected) {
//				r.begin(ShapeType.Line);
//				r.setColor(0.8f, 0.8f, 0, 1);
//				r.circle(x, y, radius);
//				r.end();
//			}
//			SpriteBatch spr = batch.getSpriteBatch();
//			BitmapFont font = batch.getFont();
//			spr.begin();
//			font.setColor(Color.BLACK);
//			font.draw(spr, String.valueOf(agent.getAP()), x, y+15);
//			if (!agent.isOpen() && agent.isFlanked()) {
//				font.setColor(Color.ORANGE);
//			}
//			String cover = "-";
//			switch (agent.getCoverType()) {
//			case FULL:
//				cover = "F";
//				break;
//			case HALF:
//				cover = "H";
//				break;
//			default:
//				cover = "-";
//				break;
//			}
//			font.draw(spr, cover, x, y);
//			spr.end();
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
		float t = -ray.origin.z/ray.direction.z;
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

	public void onRightClick(int gameX, int gameY) {
		if (UtilFunctions.isInCircle(x, y, radius, gameX, gameY)) {
			if (this.gui.getMode() == BattleGUI.Mode.TARGET_SELECT && this == this.gui.getTarget()) {
				this.gui.attack(this);
			}
		}
	}
	
}