package com.pipai.wf.artemis.system.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.pipai.wf.artemis.components.AnchoredPositionComponent;
import com.pipai.wf.artemis.components.TextComponent;
import com.pipai.wf.artemis.system.CameraUpdateSystem;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.utils.RayMapper;

public class AnchoredTextRenderingSystem extends IteratingSystem {

	private ComponentMapper<AnchoredPositionComponent> mAnchoredPosition;
	private ComponentMapper<TextComponent> mText;

	private BatchRenderingSystem batchRenderingSystem;

	private GlyphLayout glayout;

	public AnchoredTextRenderingSystem() {
		super(Aspect.all(AnchoredPositionComponent.class, TextComponent.class));
		glayout = new GlyphLayout();
	}

	@Override
	protected void process(int entityId) {
		AnchoredPositionComponent cAnchoredPosition = mAnchoredPosition.get(entityId);
		TextComponent cText = mText.get(entityId);
		BatchHelper batch = batchRenderingSystem.getBatch();
		ShapeRenderer r = batch.getShapeRenderer();
		SpriteBatch spr = batch.getSpriteBatch();
		BitmapFont font = batch.getFont();
		glayout.setText(font, cText.text);
		RayMapper mapper = new RayMapper(world.getSystem(CameraUpdateSystem.class).getCamera());
		Vector2 screenPos = mapper.pointToScreen(cAnchoredPosition.anchor.cpy());
		r.begin(ShapeType.Filled);
		r.setColor(new Color(0, 0.2f, 0.5f, 1));
		r.rect(screenPos.x, screenPos.y + font.getLineHeight(), glayout.width + 12, glayout.height + 12);
		r.end();
		spr.begin();
		font.setColor(Color.WHITE);
		font.draw(spr, cText.text, screenPos.x + 6, screenPos.y + 2 * font.getLineHeight());
		spr.end();
	}

}
