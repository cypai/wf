package com.pipai.wf.renderable.gui;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.renderable.ShapeRenderable;

/*
 * Simple 2D GUI for rendering a BattleMap
 */

public class BattleTestGUI implements ShapeRenderable{
    
	private BattleController battle;
	
	public BattleTestGUI(BattleController battle) {
		this.battle = battle;
	}
	
	public void renderShape(ShapeRenderer batch) {
		 batch.begin(ShapeType.Filled);
		 batch.setColor(1, 1, 1, 1);
		 batch.rect(10, 10, 400, 400);
		 batch.end();
	}

}