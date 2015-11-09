package com.pipai.wf.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.pipai.wf.WFGame;
import com.pipai.wf.gui.util.GuiObjectBuffers;
import com.pipai.wf.guiobject.GuiObject;
import com.pipai.wf.guiobject.GuiRenderable;
import com.pipai.wf.guiobject.LeftClickable;
import com.pipai.wf.guiobject.ui.MainMenuTestbedButton;

public class MainMenuGui extends Gui {

	private GuiObjectBuffers<GuiRenderable> renderables;
	private GuiObjectBuffers<LeftClickable> leftClickables;

	public MainMenuGui(WFGame game) {
		super(game);
		renderables = new GuiObjectBuffers<>();
		leftClickables = new GuiObjectBuffers<>();
		createInstance(new MainMenuTestbedButton(this));
	}

	@Override
	public void createInstance(GuiObject o) {
		super.createInstance(o);
		if (o instanceof GuiRenderable) {
			renderables.addToCreateBuffer((GuiRenderable) o);
		}
		if (o instanceof LeftClickable) {
			leftClickables.addToCreateBuffer((LeftClickable) o);
		}
	}

	@Override
	public void deleteInstance(GuiObject o) {
		super.deleteInstance(o);
		if (o instanceof GuiRenderable) {
			renderables.addToDeleteBuffer((GuiRenderable) o);
		}
		if (o instanceof LeftClickable) {
			leftClickables.addToDeleteBuffer((LeftClickable) o);
		}
	}

	@Override
	public void onLeftClick(int screenX, int screenY) {
		for (LeftClickable obj : leftClickables.getAll()) {
			obj.onLeftClick(screenX, getScreenHeight() - screenY);
		}
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

	@Override
	public void render(float delta) {
		super.render(delta);
		for (GuiRenderable obj : renderables.getAll()) {
			obj.render(batch);
		}
		flushCreateBuffers();
		flushDeleteBuffers();
	}

	private void flushCreateBuffers() {
		renderables.flushCreateBuffer();
		leftClickables.flushCreateBuffer();
	}

	private void flushDeleteBuffers() {
		renderables.flushDeleteBuffer();
		leftClickables.flushDeleteBuffer();
	}

	public void goToTestbed() {
		game.setScreen(new PartyInfoGui(game));
		dispose();
	}

}
