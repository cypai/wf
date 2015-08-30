package com.pipai.wf.guiobject.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;
import com.pipai.wf.battle.action.TargetedWithAccuracyAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.PercentageModifier;
import com.pipai.wf.battle.damage.PercentageModifierList;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.GUI;
import com.pipai.wf.guiobject.GUIObject;
import com.pipai.wf.guiobject.Renderable;
import com.pipai.wf.unit.ability.Ability;

public class AgentStatusWindow extends GUIObject implements Renderable {

	public enum Mode {
		AGENT_STATUS, TARGETED_ACC_STATUS;
	}

	private Mode mode;
	private boolean visible;
	private Agent agent;
	private TargetedWithAccuracyAction targetAccAction;
	private float x, y, width, height;

	private final int padding = 32;

	public AgentStatusWindow(GUI gui) {
		super(gui);
		mode = Mode.AGENT_STATUS;
		visible = false;
		x = padding;
		y = padding;
		width = gui.getScreenWidth() - padding*2;
		height = gui.getScreenHeight() - padding*2;
	}

	public void setAgentStatus(Agent a) {
		mode = Mode.AGENT_STATUS;
		agent = a;
	}

	public void setTargetedWithAccuracyAction(TargetedWithAccuracyAction a) {
		mode = Mode.TARGETED_ACC_STATUS;
		targetAccAction = a;
	}

	public boolean getVisible() {
		return visible;
	}

	public void setVisible(boolean v) {
		visible = v;
	}

	@Override
	public int renderPriority() {
		return 0;
	}

	@Override
	public void render(BatchHelper batch) {
		if (!visible) { return; }
		ShapeRenderer r = batch.getShapeRenderer();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		r.begin(ShapeType.Filled);
		r.setColor(new Color(0, 0, 0, 0.7f));
		r.rect(x, y, width, height);
		r.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		if (mode == Mode.AGENT_STATUS) {
			renderAgentMode(batch);
		} else {
			renderTAccActionMode(batch);
		}
	}

	private void renderAgentMode(BatchHelper batch) {
		SpriteBatch spr = batch.getSpriteBatch();
		BitmapFont f = batch.getFont();
		spr.begin();
		f.setColor(Color.WHITE);
		f.draw(spr, agent.getName(), padding + width/2, padding + height - f.getLineHeight(), 0, Align.center, true);
		spr.end();
		renderAbilities(batch, padding * 2, height - f.getLineHeight() * 2);
	}

	private void renderTAccActionMode(BatchHelper batch) {
		SpriteBatch spr = batch.getSpriteBatch();
		BitmapFont f = batch.getFont();
		spr.begin();
		f.setColor(Color.WHITE);
		f.draw(spr, targetAccAction.getTarget().getName(), padding + width/2, padding + height - f.getLineHeight(), 0, Align.center, true);
		spr.end();
		renderCalc(batch, padding * 2, height - f.getLineHeight() * 2, targetAccAction.getHitCalculation(), "Hit Calculation");
		renderCalc(batch, width - padding - 120, height - f.getLineHeight() * 2, targetAccAction.getCritCalculation(), "Crit Calculation");
		renderAbilities(batch, padding * 2, height - f.getLineHeight() * 12);
	}

	private void renderCalc(BatchHelper batch, float calcX, float calcY, PercentageModifierList pmList, String title) {
		ShapeRenderer r = batch.getShapeRenderer();
		SpriteBatch spr = batch.getSpriteBatch();
		BitmapFont f = batch.getFont();
		spr.begin();
		f.setColor(Color.WHITE);
		f.draw(spr, title, calcX, calcY, 0, Align.left, true);
		float bonusNameX = calcX;
		float modifierX = calcX + 120;
		float currY = calcY - f.getLineHeight() * 1.5f;
		for (PercentageModifier pm : pmList) {
			f.draw(spr, pm.modifierName, bonusNameX, currY, 0, Align.left, true);
			f.draw(spr, String.valueOf(pm.modifier), modifierX, currY, 0, Align.right, true);
			currY -= f.getLineHeight();
		}
		f.draw(spr, "Total", bonusNameX, currY - 2, 0, Align.left, true);
		f.draw(spr, String.valueOf(pmList.total()), modifierX, currY - 2, 0, Align.right, true);
		spr.end();
		r.begin(ShapeType.Line);
		r.setColor(Color.WHITE);
		r.line(bonusNameX - 8, currY + 1, modifierX + 8, currY + 1);
		r.end();
	}

	private void renderAbilities(BatchHelper batch, float abilityX, float abilityY) {
		SpriteBatch spr = batch.getSpriteBatch();
		BitmapFont f = batch.getFont();
		spr.begin();
		f.setColor(Color.WHITE);
		f.draw(spr, "Abilities", abilityX, abilityY, 0, Align.left, true);
		float currY = abilityY - f.getLineHeight() * 2;
		for (Ability a : targetAccAction.getTarget().getAbilities()) {
			f.draw(spr, a.name(), abilityX, currY, width/3, Align.left, true);
			f.draw(spr, a.description(), abilityX, currY - f.getLineHeight(), width/3, Align.left, true);
			currY -= f.getLineHeight() * 3;
		}
		spr.end();
	}

}
