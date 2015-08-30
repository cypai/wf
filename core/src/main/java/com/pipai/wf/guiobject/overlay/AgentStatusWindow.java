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
		f.draw(spr, "Agent Name Here", padding + width/2, padding + height - f.getLineHeight(), 0, Align.center, true);
		// Draw Abilities
		float abilityX = padding * 2;
		float abilityY = height - f.getLineHeight() * 2;
		f.draw(spr, "Abilities", abilityX, abilityY, 0, Align.left, true);
		float currY = abilityY - f.getLineHeight() * 2;
		for (Ability a : agent.getAbilities()) {
			f.draw(spr, a.name(), abilityX, currY, width/3, Align.left, true);
			f.draw(spr, a.description(), abilityX, currY - f.getLineHeight(), width/3, Align.left, true);
			currY -= f.getLineHeight() * 2;
		}
		spr.end();
	}

	private void renderTAccActionMode(BatchHelper batch) {
		SpriteBatch spr = batch.getSpriteBatch();
		BitmapFont f = batch.getFont();
		spr.begin();
		f.setColor(Color.WHITE);
		f.draw(spr, "Agent Name Here", padding + width/2, padding + height - f.getLineHeight(), 0, Align.center, true);
		// Draw Abilities
		float abilityX = padding * 2;
		float abilityY = height - f.getLineHeight() * 8;
		f.draw(spr, "Abilities", abilityX, abilityY, 0, Align.left, true);
		float currY = abilityY - f.getLineHeight() * 2;
		for (Ability a : targetAccAction.getTarget().getAbilities()) {
			f.draw(spr, a.name(), abilityX, currY, width/3, Align.left, true);
			f.draw(spr, a.description(), abilityX, currY - f.getLineHeight(), width/3, Align.left, true);
			currY -= f.getLineHeight() * 2;
		}
		spr.end();
	}

}
