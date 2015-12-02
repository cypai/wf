package com.pipai.wf.guiobject.mainmenu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Color;
import com.pipai.wf.gui.MainMenuGui;
import com.pipai.wf.guiobject.ui.CenteredButton;
import com.pipai.wf.save.CorruptedSaveException;
import com.pipai.wf.save.Save;
import com.pipai.wf.save.SaveManager;

public class LoadGameButton extends CenteredButton {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoadGameButton.class);

	private MainMenuGui mainMenuGui;

	public LoadGameButton(MainMenuGui gui) {
		super(gui, gui.getScreenWidth() / 2, 32, 96, 32, Color.CYAN, Color.WHITE, "Load Game");
		mainMenuGui = gui;
	}

	@Override
	protected void onLeftClickImpl() {
		SaveManager manager = new SaveManager();
		try {
			Save save = manager.load(1);
			mainMenuGui.goToTestbed(save.getParty());
		} catch (CorruptedSaveException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
