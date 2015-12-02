package com.pipai.wf.guiobject.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.pipai.wf.gui.MainMenuGui;
import com.pipai.wf.guiobject.ui.CenteredButton;

public class QuitGameButton extends CenteredButton {

	public QuitGameButton(MainMenuGui gui) {
		super(gui, gui.getScreenWidth() * 3 / 4, 32, 96, 32, Color.CYAN, Color.WHITE, "Quit Game");
	}

	@Override
	protected void onLeftClickImpl() {
		Gdx.app.exit();
	}

}
