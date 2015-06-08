package com.pipai.wf.guiobject.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.BattleGUI;
import com.pipai.wf.guiobject.GUIObject;
import com.pipai.wf.guiobject.Renderable;
import com.pipai.wf.guiobject.battle.AgentGUIObject;

public class WeaponIndicator extends GUIObject implements Renderable {
	
	protected BattleGUI gui;
	private float x, y, width, height;
	private Weapon primary;

	public WeaponIndicator(BattleGUI gui, float x, float y, float width, float height) {
		super(gui);
		this.gui = gui;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void updateToAgent(AgentGUIObject a) {
		primary = a.getAgent().getCurrentWeapon();
	}

	@Override
	public int renderPriority() {
		return 0;
	}

	@Override
	public void render(BatchHelper batch) {
		if (gui.getMode() != BattleGUI.Mode.NONE && gui.getMode() != BattleGUI.Mode.TARGET_SELECT) {
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
		f.setColor(Color.WHITE);
		String primaryInfo = primary.name() + "   " + String.valueOf(primary.currentAmmo()) + "/" + String.valueOf(primary.baseAmmoCapacity());
		f.draw(spr, primaryInfo, x + f.getLineHeight(), y - f.getLineHeight(), 0, Align.left, true);
		spr.end();
	}

}
