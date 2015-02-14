package com.pipai.wf;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.pipai.wf.screen.BattleTestbedScreen;

public class WFGame extends Game {

    public ShapeRenderer batch;
    public BitmapFont font;
    
    @Override
    public void create() {
    	this.batch = new ShapeRenderer();
    	this.font = new BitmapFont();
        this.setScreen(new BattleTestbedScreen(this));
    }

	@Override
	public void render() {
		super.render();
	}
}
