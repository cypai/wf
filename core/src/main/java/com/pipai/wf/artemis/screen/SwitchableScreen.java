package com.pipai.wf.artemis.screen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public abstract class SwitchableScreen implements Screen {

	private static final Logger LOGGER = LoggerFactory.getLogger(SwitchableScreen.class);

	private Game game;

	public SwitchableScreen(Game game) {
		this.game = game;
	}

	public void switchScreen(Screen screen) {
		LOGGER.debug("Switching Gui to " + screen.getClass());
		dispose();
		game.setScreen(screen);
	}

}
