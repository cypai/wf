package com.pipai.wf.artemis.system.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pipai.wf.artemis.components.XYPositionComponent;
import com.pipai.wf.artemis.components.textbox.PartialTextComponent;
import com.pipai.wf.artemis.components.textbox.TextBoxComponent;

public class PartialTextBoxRenderingSystem extends IteratingSystem {

	private ComponentMapper<PartialTextComponent> mPartialText;
	private ComponentMapper<TextBoxComponent> mTextBox;
	private ComponentMapper<XYPositionComponent> mXy;

	private BatchRenderingSystem batchRenderingSystem;

	public PartialTextBoxRenderingSystem() {
		super(Aspect.all(PartialTextComponent.class, TextBoxComponent.class, XYPositionComponent.class));
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void process(int entityId) {
		if (checkProcessing()) {
			PartialTextComponent cPartialText = mPartialText.get(entityId);
			TextBoxComponent cTextBox = mTextBox.get(entityId);
			XYPositionComponent cXy = mXy.get(entityId);

			float left = cXy.position.x - cTextBox.width / 2;
			// float right = cXy.position.x + cTextBox.width / 2;
			float up = cXy.position.y + cTextBox.height / 2;
			float down = cXy.position.y - cTextBox.height / 2;

			ShapeRenderer batch = batchRenderingSystem.getBatch().getShapeRenderer();
			batch.begin(ShapeType.Filled);
			batch.setColor(Color.DARK_GRAY);
			batch.rect(left, down, cTextBox.width, cTextBox.height);
			batch.end();

			SpriteBatch spr = batchRenderingSystem.getBatch().getSpriteBatch();
			BitmapFont font = batchRenderingSystem.getBatch().getFont();
			spr.begin();
			font.setColor(Color.WHITE);
			font.draw(spr, cPartialText.currentText, left + 6, up - font.getLineHeight());
			spr.end();

		}
	}

}
