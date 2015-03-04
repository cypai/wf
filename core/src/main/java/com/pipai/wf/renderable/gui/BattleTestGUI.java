package com.pipai.wf.renderable.gui;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pipai.wf.battle.Agent;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.renderable.ShapeRenderable;

/*
 * Simple 2D GUI for rendering a BattleMap
 */

public class BattleTestGUI implements ShapeRenderable {
    
	public static final int SQUARE_SIZE = 40;
	
	private BattleController battle;
	private Agent selectedAgent;
	
	public BattleTestGUI(BattleController battle) {
		this.battle = battle;
	}
	
	public void onLeftClick(int screenX, int screenY, int gameX, int gameY) {
		for (Agent agent : this.battle.getBattleMap().getAgents()) {
			if (agent.getTeam() == Agent.Team.PLAYER && this.withinCircularBounds(agent.getPosition(), gameX, gameY)) {
				this.selectedAgent = agent;
				break;
			}
		}
	}
	
	public void onRightClick(int screenX, int screenY, int gameX, int gameY) {
		if (this.selectedAgent != null) {
			GridPosition moveSquare = this.gamePosToGridPos(gameX, gameY);
			MoveAction move = new MoveAction(this.selectedAgent, moveSquare);
			move.perform();
		}
	}
	
	private boolean withinCircularBounds(GridPosition pos, int gameX, int gameY) {
		return (gameX > pos.x * SQUARE_SIZE) && (gameX < (pos.x+1) * SQUARE_SIZE) && (gameY > pos.y * SQUARE_SIZE) && (gameY < (pos.y+1) * SQUARE_SIZE);
	}
	
	private GridPosition gamePosToGridPos(int gameX, int gameY) {
		int x_offset = gameX % SQUARE_SIZE;
		int y_offset = gameY % SQUARE_SIZE;
		return new GridPosition((gameX - x_offset)/SQUARE_SIZE, (gameY - y_offset)/SQUARE_SIZE);
	}
	
	public void renderShape(ShapeRenderer batch, int width, int height) {
		BattleMap map = this.battle.getBattleMap();
		
		this.drawGrid(batch, 0, 0, SQUARE_SIZE * map.getCols(), SQUARE_SIZE * map.getRows(), map.getCols(), map.getRows());
		this.drawAgents(batch, 0, 0, map);
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
	
	private void drawAgents(ShapeRenderer batch, float gridX, float gridY, BattleMap map) {
		batch.begin(ShapeType.Filled);
		for (Agent agent : map.getAgents()) {
			if (agent.getTeam() == Agent.Team.PLAYER) {
				batch.setColor(0, 0.8f, 0, 1);
			} else {
				batch.setColor(0.8f, 0, 0, 1);
			}
			batch.circle(agent.getPosition().x * SQUARE_SIZE + SQUARE_SIZE/2, agent.getPosition().y * SQUARE_SIZE + SQUARE_SIZE/2, SQUARE_SIZE/2);
		}
		batch.end();
		if (this.selectedAgent != null) {
			batch.begin(ShapeType.Line);
			batch.setColor(1, 1, 0, 1);
			batch.circle(this.selectedAgent.getPosition().x * SQUARE_SIZE + SQUARE_SIZE/2, this.selectedAgent.getPosition().y * SQUARE_SIZE + SQUARE_SIZE/2, SQUARE_SIZE/2);
			batch.end();
		}
	}

}