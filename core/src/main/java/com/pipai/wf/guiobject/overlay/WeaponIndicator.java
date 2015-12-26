package com.pipai.wf.guiobject.overlay;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.battle.weapon.SpellWeapon;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.BattleGui;
import com.pipai.wf.guiobject.GuiObject;
import com.pipai.wf.guiobject.GuiRenderable;
import com.pipai.wf.guiobject.battle.AgentGuiObject;

public class WeaponIndicator extends GuiObject implements GuiRenderable {

	private BattleGui gui;
	private float x, y, width, height;
	private ArrayList<Weapon> weapons;
	private AgentGuiObject agent;

	public WeaponIndicator(BattleGui gui, float x, float y, float width, float height) {
		super(gui);
		this.gui = gui;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		weapons = new ArrayList<>();
	}

	public void updateToAgent(AgentGuiObject a) {
		weapons = a.getAgent().getWeapons();
		agent = a;
	}

	@Override
	public int renderPriority() {
		return 0;
	}

	@Override
	public void render(BatchHelper batch) {
		if (gui.getMode() != BattleGui.Mode.MOVE && gui.getMode() != BattleGui.Mode.TARGET_SELECT) {
			return;
		}
		ShapeRenderer r = batch.getShapeRenderer();
		SpriteBatch spr = batch.getSpriteBatch();
		BitmapFont f = batch.getFont();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		r.begin(ShapeType.Filled);
		r.setColor(new Color(0.5f, 0.5f, 0.5f, 0.7f));
		r.rect(x, y, width, -height);
		r.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		spr.begin();
		int line = 1;
		for (Weapon weapon : weapons) {
			String weaponInfo;
			if (weapon instanceof SpellWeapon) {
				weaponInfo = weapon.getName() + "   ";
				Spell spell = ((SpellWeapon) weapon).getSpell();
				if (spell == null) {
					weaponInfo += "-";
				} else {
					weaponInfo += spell.getName();
				}
			} else {
				weaponInfo = weapon.getName() + "   " + String.valueOf(weapon.currentAmmo()) + "/" + String.valueOf(weapon.baseAmmoCapacity());
			}
			if (weapon == agent.getAgent().getCurrentWeapon()) {
				f.setColor(Color.ORANGE);
			} else {
				f.setColor(Color.WHITE);
			}
			f.draw(spr, weaponInfo, x + f.getLineHeight(), y - f.getLineHeight() * line, 0, Align.left, true);
			line += 1;
		}
		spr.end();
	}

}
