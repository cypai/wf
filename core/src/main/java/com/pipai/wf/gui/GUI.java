package com.pipai.wf.gui;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.pipai.wf.WFGame;
import com.pipai.wf.guiobject.GUIObject;

public abstract class GUI implements Screen, InputProcessor {
	
	protected WFGame game;
	protected BatchHelper batch;
	protected int width, height;
	protected ConcurrentHashMap<Integer, GUIObject> instanceIndex;
	//Maybe add BST for rendering order??
	
	//Input Processing
	private HashMap<Integer, Boolean> heldKeys;
	
	public GUI(WFGame game) {
		this.game = game;
        Gdx.input.setInputProcessor(this);
        batch = new BatchHelper(game.sprBatch, game.shapeBatch, game.modelBatch, game.font);
		instanceIndex = new ConcurrentHashMap<Integer, GUIObject>();
        heldKeys = new HashMap<Integer, Boolean>();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
	}
	
	public int getScreenWidth() { return width; }
	public int getScreenHeight() { return height; }
	
	public void createInstance(GUIObject o) {
		instanceIndex.put(o.getID(), o);
	}
	public void deleteInstance(GUIObject o) {
		instanceIndex.remove(o.getID());
		o.dispose();
	}
	public void deleteInstance(int id) {
		instanceIndex.remove(id);
	}
	
	public boolean checkKey(int keycode) {
		if (!this.heldKeys.containsKey(keycode)) {
			return false;
		} else {
			return this.heldKeys.get(keycode);
		}
	}

	@Override
	public void show() {
		
	}
	
	@Override
	public void render(float delta) {
		for (GUIObject o : this.instanceIndex.values()) {
			o.update();
		}
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
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
		for (GUIObject o : this.instanceIndex.values()) {
			o.dispose();
		}
	}

	@Override
	public final boolean keyDown(int keycode) {
		this.heldKeys.put(keycode, true);
		this.onKeyDown(keycode);
        return true;
	}

	@Override
	public final boolean keyUp(int keycode) {
		this.heldKeys.put(keycode, false);
		this.onKeyUp(keycode);
		return true;
	}

	@Override
	public final boolean keyTyped(char character) {
		return false;
	}

	public abstract void onLeftClick(int screenX, int screenY);
	public abstract void onRightClick(int screenX, int screenY);
	
	public abstract void onKeyDown(int keycode);
	public abstract void onKeyUp(int keycode);
	
	public abstract void mouseScrolled(int amount);
	
	@Override
	public final boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.LEFT) {
			onLeftClick(screenX, screenY);
		} else if (button == Buttons.RIGHT) {
			onRightClick(screenX, screenY);
		}
		return true;
	}

	@Override
	public final boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public final boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public final boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public final boolean scrolled(int amount) {
		mouseScrolled(amount);
		return true;
	}
	
}