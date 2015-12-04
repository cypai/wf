package com.pipai.wf.guiobject.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.Gui;
import com.pipai.wf.misc.BasicStats;
import com.pipai.wf.unit.schema.UnitSchema;

public class UnitInfoListItem extends UIListItem {

	// public static final int PADDING = 4;
	// public static final int STAT_SPACING = 64;
	// public static final int WIDTH = 2 * PADDING + STAT_SPACING * 6;
	// public static final int HEIGHT = 2 * PADDING + 32;

	private String name;
	private BasicStats stats;
	private float width, padding, stat_spacing, height;
	private ArrayList<String> statLine;

	public UnitInfoListItem(Gui gui, UnitSchema schema, float x, float y, float width, float padding) {
		super(gui, x, y, width, 2 * padding + 32, Color.ORANGE);
		this.width = width;
		this.padding = padding;
		stat_spacing = (width - 2 * padding) / 6;
		height = 2 * padding + 32;
		stats = schema.getBasicStats();
		name = schema.getName();
		statLine = new ArrayList<String>();
		statLine.add("HP: " + stats.getHP() + "/" + stats.getMaxHP());
		statLine.add("MP: " + stats.getMP() + "/" + stats.getMaxMP());
		statLine.add("");	// Hack for buffer between HP/MP and immutable stats
		statLine.add("Mob: " + stats.getMobility());
		statLine.add("Aim: " + stats.getAim());
		statLine.add("Def: " + stats.getDefense());
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	@Override
	public void render(BatchHelper batch) {
		super.render(batch);
		SpriteBatch spr = batch.getSpriteBatch();
		BitmapFont font = batch.getFont();
		spr.begin();
		font.setColor(Color.WHITE);
		float leftX = getX() + padding;
		float line1y = getY() - padding;
		float line2y = line1y - font.getLineHeight();
		font.draw(spr, name, leftX, line1y);
		float textX = leftX;
		for (String s : statLine) {
			font.draw(spr, s, textX, line2y);
			textX += stat_spacing;
		}
		spr.end();
	}

}
