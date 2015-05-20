package com.pipai.wf.guiobject.ui;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.GUI;

public class UnitInfoListItem extends UIListItem {
	
//	public static final int PADDING = 4;
//	public static final int STAT_SPACING = 64;
//	public static final int WIDTH = 2 * PADDING + STAT_SPACING * 6;
//	public static final int HEIGHT = 2 * PADDING + 32;
	
	protected String name;
	protected int hp, mp, ap, mobility, aim, defense;
	protected float width, padding, stat_spacing, height;
	protected LinkedList<String> statLine;
	
	public UnitInfoListItem(GUI gui, AgentState state, float x, float y, float width, float padding) {
		super(gui, x, y, width, 2 * padding + 32, Color.ORANGE);
		this.width = width;
		this.padding = padding;
		this.stat_spacing = (width - 2 * padding)/6;
		this.height = 2 * padding + 32;
		hp = state.maxHP;
		mp = state.maxMP;
		ap = state.maxAP;
		mobility = state.mobility;
		aim = state.aim;
		defense = state.defense;
		name = "Tidus";
		statLine = new LinkedList<String>();
		statLine.add("HP: " + String.valueOf(hp));
		statLine.add("MP: " + String.valueOf(mp));
		statLine.add("AP: " + String.valueOf(ap));
		statLine.add("Mob: " + String.valueOf(mobility));
		statLine.add("Aim: " + String.valueOf(aim));
		statLine.add("Def: " + String.valueOf(defense));
	}
	
	@Override
	public void render(BatchHelper batch) {
		super.render(batch);
		SpriteBatch spr = batch.getSpriteBatch();
		BitmapFont font = batch.getFont();
		spr.begin();
		font.setColor(Color.WHITE);
		float leftX = x + padding;
		float line1y = y - padding;
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
