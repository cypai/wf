package com.pipai.wf.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pipai.wf.WFGame;

public final class DesktopLauncher {

	private DesktopLauncher() {
	}

	// SUPPRESS CHECKSTYLE UncommentedMain Entry point
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1024;
		config.height = 768;
		new LwjglApplication(new WFGame(), config);
	}
}
