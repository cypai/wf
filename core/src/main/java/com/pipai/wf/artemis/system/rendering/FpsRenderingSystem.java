package com.pipai.wf.artemis.system.rendering;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.pipai.wf.gui.BatchHelper;

public class FpsRenderingSystem extends BaseSystem {

	private BatchRenderingSystem batchRenderingSystem;

	@Override
	protected void processSystem() {
		BatchHelper batch = batchRenderingSystem.getBatch();
		BitmapFont font = batch.getFont();
		batch.getSpriteBatch().begin();
		font.setColor(Color.WHITE);
		font.draw(batch.getSpriteBatch(),
				String.valueOf(Gdx.graphics.getFramesPerSecond()),
				Gdx.graphics.getWidth() - 24,
				Gdx.graphics.getHeight() - font.getLineHeight() / 2);
		batch.getSpriteBatch().end();
	}
}
