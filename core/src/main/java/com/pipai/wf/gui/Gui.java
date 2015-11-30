package com.pipai.wf.gui;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.pipai.wf.WFGame;
import com.pipai.wf.guiobject.GuiObject;

public abstract class Gui implements Screen, InputProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(Gui.class);

	protected WFGame game;
	protected BatchHelper batch;
	protected int width, height;
	protected ConcurrentHashMap<Integer, GuiObject> instanceIndex;
	// Maybe add BST for rendering order??

	// Input Processing
	private HashMap<Integer, Boolean> heldKeys;

	public Gui(WFGame game) {
		this.game = game;
		Gdx.input.setInputProcessor(this);
		batch = new BatchHelper(game.sprBatch, game.shapeBatch, game.modelBatch, game.font);
		instanceIndex = new ConcurrentHashMap<Integer, GuiObject>();
		heldKeys = new HashMap<Integer, Boolean>();
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
	}

	public int getScreenWidth() {
		return width;
	}

	public int getScreenHeight() {
		return height;
	}

	public void createInstance(GuiObject o) {
		instanceIndex.put(o.getID(), o);
	}

	public void deleteInstance(GuiObject o) {
		instanceIndex.remove(o.getID());
		o.dispose();
	}

	public void deleteInstance(int id) {
		instanceIndex.remove(id);
	}

	public void switchGui(Gui gui) {
		LOGGER.debug("Switching Gui to " + gui.getClass());
		dispose();
		game.setScreen(gui);
	}

	public boolean checkKey(int keycode) {
		if (heldKeys.containsKey(keycode)) {
			return heldKeys.get(keycode);
		} else {
			return false;
		}
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		for (GuiObject o : instanceIndex.values()) {
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
		for (GuiObject o : instanceIndex.values()) {
			o.dispose();
		}
	}

	@Override
	public final boolean keyDown(int keycode) {
		heldKeys.put(keycode, true);
		onKeyDown(keycode);
		return true;
	}

	@Override
	public final boolean keyUp(int keycode) {
		heldKeys.put(keycode, false);
		onKeyUp(keycode);
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