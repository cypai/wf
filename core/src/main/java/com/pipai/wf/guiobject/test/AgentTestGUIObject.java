package com.pipai.wf.guiobject.test;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.guiobject.GUIObject;
import com.pipai.wf.renderable.BatchHelper;
import com.pipai.wf.renderable.Renderable;
import com.pipai.wf.renderable.gui.BattleTestGUI;
import com.pipai.wf.renderable.gui.LeftClickable;
import com.pipai.wf.renderable.gui.RightClickable;

public class AgentTestGUIObject extends GUIObject implements Renderable, LeftClickable, RightClickable {
	
	private BattleTestGUI gui;
	private Agent agent;
	private boolean selected, ko;
	public float x, y;
	public int radius;
	
	//Animation variables
	private boolean animating;
	private LinkedList<Vector2> moveSeq;
	private Vector2 start, dest;
	private int t;	//Animation time t counter
	
	public AgentTestGUIObject(BattleTestGUI gui, Agent agent, float x, float y, int radius) {
		super(gui);
		this.gui = gui;
		this.agent = agent;
		this.selected = false;
		this.x = x;
		this.y = y;
		this.radius = radius;
		animating = false;
		ko = false;
	}
	
	public Agent getAgent() { return this.agent; }
	
	public void select() {
		selected = true; 
		gui.setSelected(this);
	}
	public void deselect() { selected = false; }
	public boolean isSelected() { return selected; }
	
	public void hit() {
		if (this.agent.isKO()) { ko = true; }
	}
	
	public void animateMoveSequence(LinkedList<Vector2> seq) {
		animating = true;
		moveSeq = seq;
		animateNextMoveInSeq();
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
		if (animating) {
			t += 1;
			int time = 6;
			if (t <= time) {
				float alpha = (float)t/(float)time;
				x = start.x*(1-alpha) + dest.x*(alpha);
				y = start.y*(1-alpha) + dest.y*(alpha);
				if (t == time) {
					animateNextMoveInSeq();
				}
			}
		}
	}

	public void render(BatchHelper batch) {
		update();
		ShapeRenderer r = batch.getShapeRenderer();
		if (!ko) {
			r.begin(ShapeType.Filled);
			if (agent.getTeam() == Agent.Team.PLAYER) {
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
	
	private boolean isWithinCircle(int gameX, int gameY) {
		return Math.pow(gameX - x, 2.0) + Math.pow(gameY - y, 2.0) < radius*radius;
	}

	public void onLeftClick(int screenX, int screenY, int gameX, int gameY) {
		if (this.agent.getTeam() == Agent.Team.PLAYER && isWithinCircle(gameX, gameY)) {
			this.select();
		}
	}

	public void onRightClick(int gameX, int gameY) {
		if (isWithinCircle(gameX, gameY)) {
			if (agent.getTeam() == Agent.Team.ENEMY) {
				gui.attack(this);
				gui.updatePaths();
			}
		}
	}
	
}