package com.pipai.wf.renderable.gui;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pipai.wf.battle.Agent;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.renderable.ShapeRenderable;

/*
 * Simple 2D GUI for rendering a BattleMap
 */

public class BattleTestGUI implements ShapeRenderable {
    
	public static final int SQUARE_SIZE = 40;
	
	private BattleController battle;
	
	public BattleTestGUI(BattleController battle) {
		this.battle = battle;
	}
	
	public void renderShape(ShapeRenderer batch, int width, int height) {
		BattleMap map = battle.getBattleMap();
		
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
			if (agent.getTeam() == Agent.Team.PLAYER) 
				batch.setColor(0, 0.8f, 0, 1);
			else
				batch.setColor(0.8f, 0, 0, 1);
			batch.circle(agent.getPosition().x * SQUARE_SIZE + SQUARE_SIZE/2, agent.getPosition().y * SQUARE_SIZE + SQUARE_SIZE/2, SQUARE_SIZE/2);
		}
		batch.end();
	}

}