package com.pipai.wf.artemis.system.battleui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class IconButton extends Button {

	private static final float WIDTH = 32;
	private static final float HEIGHT = 32;

	public IconButton(ButtonStyle style, String imagePath) {
		super(style);
		super.padTop(4);
		super.padBottom(4);
		super.padLeft(4);
		super.padRight(4);
		super.add(new Label("", new LabelStyle()));
		// Image image = new Image(new TextureRegion(new Texture(Gdx.files.internal(imagePath))));
		// image.setScale(WIDTH / image.getImageWidth(), HEIGHT / image.getImageHeight());
		// super.add(image);
	}

}
