package com.pipai.wf.guiobject.overlay;

public class TemporaryText {

	// private int destroyAlarm;
	// private String text;
	// private GlyphLayout glayout;
	//
	// public TemporaryText(BattleGui gui, Vector3 anchorPoint, String text) {
	// super(gui, gui.getRayMapper(), anchorPoint);
	// glayout = new GlyphLayout();
	// this.text = text;
	// destroyAlarm = 120;
	// }
	//
	// @Override
	// public int renderPriority() {
	// return -1;
	// }
	//
	// @Override
	// public void update() {
	// super.update();
	// destroyAlarm--;
	// if (destroyAlarm <= 0) {
	// getGui().deleteInstance(this);
	// }
	// }
	//
	// @Override
	// public void render(BatchHelper batch) {
	// ShapeRenderer r = batch.getShapeRenderer();
	// SpriteBatch spr = batch.getSpriteBatch();
	// BitmapFont font = batch.getFont();
	// glayout.setText(font, text);
	// r.begin(ShapeType.Filled);
	// r.setColor(new Color(0, 0.2f, 0.5f, 1));
	// r.rect(getScreenX(), getScreenY(), glayout.width + 12, glayout.height + 12);
	// r.end();
	// spr.begin();
	// font.setColor(Color.WHITE);
	// font.draw(spr, text, getScreenX() + 6, getScreenY() + font.getLineHeight());
	// spr.end();
	// }

}
