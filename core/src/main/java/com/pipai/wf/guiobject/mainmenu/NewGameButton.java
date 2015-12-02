package com.pipai.wf.guiobject.mainmenu;

import com.badlogic.gdx.graphics.Color;
import com.pipai.wf.gui.MainMenuGui;
import com.pipai.wf.guiobject.ui.CenteredButton;
import com.pipai.wf.save.NewGameSaveGenerator;
import com.pipai.wf.save.Save;

public class NewGameButton extends CenteredButton {

	private MainMenuGui mainMenuGui;

	public NewGameButton(MainMenuGui gui) {
		super(gui, gui.getScreenWidth() / 4, 32, 96, 32, Color.CYAN, Color.WHITE, "New Game");
		mainMenuGui = gui;
	}

	@Override
	protected void onLeftClickImpl() {
		NewGameSaveGenerator generator = new NewGameSaveGenerator();
		Save save = generator.generateNewSave();
		mainMenuGui.goToTestbed(save.getParty());
	}

}
