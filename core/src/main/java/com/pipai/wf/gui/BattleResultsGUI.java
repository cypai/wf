package com.pipai.wf.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pipai.wf.WFGame;

public class BattleResultsGUI extends GUI {
	
	private OrthographicCamera camera;

	public BattleResultsGUI(WFGame game) {
		super(game);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, this.getScreenWidth(), this.getScreenHeight());
	}
	
	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.getSpriteBatch().setProjectionMatrix(camera.combined);
        batch.getShapeRenderer().setProjectionMatrix(camera.combined);
		SpriteBatch spr = batch.getSpriteBatch();
		BitmapFont font = batch.getFont();
		spr.begin();
		font.setColor(Color.WHITE);
		font.draw(spr, "Battle Results", 24, this.getScreenHeight() - 24);
		spr.end();
	}

	@Override
	public void onLeftClick(int screenX, int screenY) {
		this.game.setScreen(new PartyInfoGUI(this.game));
		this.dispose();
	}

	@Override
	public void onRightClick(int screenX, int screenY) {
		
	}

	@Override
	public void onKeyDown(int keycode) {
		if (keycode == Keys.ESCAPE) { 
			Gdx.app.exit();
		}
	}

	@Override
	public void onKeyUp(int keycode) {
		
	}

	@Override
	public void mouseScrolled(int amount) {
		
	}
	
}
