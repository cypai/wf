package com.pipai.wf.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.pipai.wf.WFGame;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.renderable.gui.BattleTestGUI;

public class BattleTestbedScreen implements Screen {
	
	private WFGame game;
	private OrthographicCamera camera;
	private BattleTestGUI gui;
	
	public BattleTestbedScreen(WFGame game) {
		this.game = game;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 600);
        this.gui = new BattleTestGUI(new BattleController(new BattleMap(10, 10)));
	}

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.camera.update();
        this.game.batch.setProjectionMatrix(camera.combined);
        this.gui.renderShape(this.game.batch);
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		
	}
	
	
	
}