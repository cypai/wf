package com.pipai.wf.guiobject.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.renderable.BatchHelper;
import com.pipai.wf.renderable.Renderable;
import com.pipai.wf.renderable.gui.BattleTestGUI;
import com.pipai.wf.renderable.gui.LeftClickable;
import com.pipai.wf.renderable.gui.RightClickable;

public class AgentGUIObject implements Renderable, LeftClickable, RightClickable {
	
	private BattleTestGUI gui;
	private Agent agent;
	private boolean selected;
	
	public AgentGUIObject(BattleTestGUI gui, Agent agent) {
		this.gui = gui;
		this.agent = agent;
		this.selected = false;
	}
	
	public void select() {
		selected = true; 
		gui.setSelected(agent);
	}
	public void deselect() { selected = false; }
	public boolean isSelected() { return selected; }

	public void render(BatchHelper batch, int width, int height) {
		int SQUARE_SIZE = BattleTestGUI.SQUARE_SIZE;
		GridPosition pos = agent.getPosition();
		ShapeRenderer r = batch.getShapeRenderer();
		if (!agent.isKO()) {
			r.begin(ShapeType.Filled);
			if (agent.getTeam() == Agent.Team.PLAYER) {
				r.setColor(0, 0.8f, 0, 1);
			} else {
				r.setColor(0.8f, 0, 0, 1);
			}
			r.circle(pos.x * SQUARE_SIZE + SQUARE_SIZE/2, pos.y * SQUARE_SIZE + SQUARE_SIZE/2, SQUARE_SIZE/2);
			r.end();
			if (this.selected) {
				r.begin(ShapeType.Line);
				r.setColor(0.8f, 0.8f, 0, 1);
				r.circle(pos.x * SQUARE_SIZE + SQUARE_SIZE/2, pos.y * SQUARE_SIZE + SQUARE_SIZE/2, SQUARE_SIZE/2);
				r.end();
			}
			SpriteBatch spr = batch.getSpriteBatch();
			BitmapFont font = batch.getFont();
			spr.begin();
			font.setColor(Color.BLACK);
			font.draw(spr, String.valueOf(agent.getAP()), pos.x*SQUARE_SIZE, (pos.y+1)*SQUARE_SIZE);
			font.draw(spr, String.valueOf(agent.getHP()), pos.x*SQUARE_SIZE, (pos.y+0.5f)*SQUARE_SIZE);
			spr.end();
		}
	}

	public void onLeftClick(int gameX, int gameY) {
		if (this.agent.getTeam() == Agent.Team.PLAYER && BattleTestGUI.withinGridBounds(agent.getPosition(), gameX, gameY)) {
			this.select();
		}
	}

	public void onRightClick(int gameX, int gameY) {
		if (BattleTestGUI.withinGridBounds(agent.getPosition(), gameX, gameY)) {
			if (agent.getTeam() == Agent.Team.ENEMY) {
				gui.attack(agent);
				gui.updatePaths();
			}
		}
	}
	
}