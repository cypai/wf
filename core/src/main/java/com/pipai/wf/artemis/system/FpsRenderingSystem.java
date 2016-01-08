package com.pipai.wf.artemis.system;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.pipai.wf.gui.BatchHelper;

public class FpsRenderingSystem extends BaseSystem {

	private BatchHelper batch;

	public FpsRenderingSystem(BatchHelper batch) {
		this.batch = batch;
	}

	private BatchHelper getBatch() {
		return batch;
	}

	@Override
	protected void processSystem() {
		BitmapFont font = getBatch().getFont();
		getBatch().getSpriteBatch().begin();
		font.setColor(Color.WHITE);
		font.draw(getBatch().getSpriteBatch(),
				String.valueOf(Gdx.graphics.getFramesPerSecond()),
				Gdx.graphics.getWidth() - 24,
				Gdx.graphics.getHeight() - font.getLineHeight() / 2);
		getBatch().getSpriteBatch().end();
	}
}
