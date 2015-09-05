package com.pipai.wf;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.pipai.wf.config.WFConfig;
import com.pipai.wf.gui.PartyInfoGui;

public class WFGame extends Game {
	
	public SpriteBatch sprBatch;
    public ShapeRenderer shapeBatch;
    public ModelBatch modelBatch;
    public BitmapFont font;
    
    @Override
    public void create() {
    	WFConfig.resetConfig();
    	this.sprBatch = new SpriteBatch();
    	this.shapeBatch = new ShapeRenderer();
    	this.modelBatch = new ModelBatch();
    	this.font = new BitmapFont();
        this.setScreen(new PartyInfoGui(this));
    }

	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose() {
		sprBatch.dispose();
		shapeBatch.dispose();
		modelBatch.dispose();
		font.dispose();
	}
	
}
