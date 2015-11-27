package com.pipai.wf.guiobject.ui;

import com.badlogic.gdx.graphics.Color;
import com.pipai.wf.gui.MainMenuGui;
import com.pipai.wf.save.SaveManager;

public class NewGameButton extends CenteredButton {

	private MainMenuGui mainMenuGui;

	public NewGameButton(MainMenuGui gui) {
		super(gui, gui.getScreenWidth() / 4, 32, 64, 32, Color.CYAN, Color.WHITE, "New Game");
		mainMenuGui = gui;
	}

	@Override
	protected void onLeftClickImpl() {
		SaveManager manager = new SaveManager("");
		// mainMenuGui.go
	}

}
