package com.pipai.wf.guiobject.battle;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.BattleGUI;
import com.pipai.wf.guiobject.GUIObject;
import com.pipai.wf.guiobject.LeftClickable;
import com.pipai.wf.guiobject.Renderable;
import com.pipai.wf.guiobject.RightClickable;
import com.pipai.wf.util.Alarm;
import com.pipai.wf.util.UtilFunctions;

public class AgentGUIObject extends GUIObject implements Renderable, LeftClickable, RightClickable {
	
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
				Vector2 owtile = BattleGUI.centerOfGridPos(ev.getTargetTile());
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
		ShapeRenderer r = batch.getShapeRenderer();
		if (!ko) {
			r.begin(ShapeType.Filled);
			if (agent.getTeam() == Team.PLAYER) {
				r.setColor(0, 0.8f, 0, 1);
			} else {
				r.setColor(0.8f, 0, 0, 1);
			}
			r.circle(x, y, radius);
			r.end();
			if (this.selected) {
				r.begin(ShapeType.Line);
				r.setColor(0.8f, 0.8f, 0, 1);
				r.circle(x, y, radius);
				r.end();
			}
			SpriteBatch spr = batch.getSpriteBatch();
			BitmapFont font = batch.getFont();
			spr.begin();
			font.setColor(Color.BLACK);
			font.draw(spr, String.valueOf(agent.getAP()), x, y+15);
			font.draw(spr, String.valueOf(agent.getHP()), x, y);
			spr.end();
		}
	}

	public void onLeftClick(int screenX, int screenY, int gameX, int gameY) {
		if (this.agent.getTeam() == Team.PLAYER && UtilFunctions.isInCircle(x, y, radius, gameX, gameY)) {
			this.select();
		}
	}

	public void onRightClick(int gameX, int gameY) {
		if (UtilFunctions.isInCircle(x, y, radius, gameX, gameY)) {
			if (agent.getTeam() == Team.ENEMY) {
				gui.attack(this);
				gui.updatePaths();
			}
		}
	}
	
}