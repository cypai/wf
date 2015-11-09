package com.pipai.wf.guiobject.ui;

import com.badlogic.gdx.graphics.Color;
import com.pipai.wf.gui.MainMenuGui;

public class MainMenuTestbedButton extends CenteredButton {

	private MainMenuGui mainMenuGui;

	public MainMenuTestbedButton(MainMenuGui gui) {
		super(gui, gui.getScreenWidth() / 2, 32, 64, 32, Color.CYAN, Color.WHITE, "Testbed");
		mainMenuGui = gui;
	}

	@Override
	protected void onLeftClickImpl() {
		mainMenuGui.goToTestbed();
	}

}
