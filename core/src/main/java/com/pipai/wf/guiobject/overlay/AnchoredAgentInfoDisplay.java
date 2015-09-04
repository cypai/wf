package com.pipai.wf.guiobject.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.battle.Team;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.BattleGUI;
import com.pipai.wf.guiobject.AnchoredGUIObject;
import com.pipai.wf.guiobject.battle.AgentGUIObject;

public class AnchoredAgentInfoDisplay extends AnchoredGUIObject {

	protected AgentGUIObject a;

	public AnchoredAgentInfoDisplay(BattleGUI gui, AgentGUIObject agent) {
		super(gui, gui.getRayMapper(), new Vector3(agent.x, agent.y, 0));
		this.a = agent;
	}

	@Override
	public int renderPriority() { return -1; }

	@Override
	public void render(BatchHelper batch) {
		if (a.isShowingKO()) {
			return;
		}
		ShapeRenderer shapeBatch = batch.getShapeRenderer();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeBatch.begin(ShapeType.Filled);
		int bar_width = 40;
		Vector2 agentPoint = screenPosition;
		Vector2 barLeftTop = new Vector2(screenPosition.x + 24, screenPosition.y + 24);
		Vector2 barRightFullTop = new Vector2(screenPosition.x + 24 + bar_width, screenPosition.y + 24);
		Vector2 barRightTop = new Vector2(screenPosition.x + 24 + bar_width * ((float)a.getDisplayArmorHP() / (float)a.getAgent().getArmor().maxHP()), screenPosition.y + 24);
		Vector2 barLeftBot = new Vector2(screenPosition.x + 24, screenPosition.y + 18);
		Vector2 barRightFullBot = new Vector2(screenPosition.x + 24 + bar_width, screenPosition.y + 18);
		Vector2 barRightBot = new Vector2(screenPosition.x + 24 + bar_width * ((float)a.getDisplayHP() / (float)a.getAgent().getMaxHP()), screenPosition.y + 18);
		float alpha = (a.getAgent().getTeam() == Team.PLAYER && a.getAgent().getAP() == 0) ? -0.5f : 0;
		shapeBatch.setColor(Color.BLUE.cpy().add(0, 0, 0, alpha));
		shapeBatch.rectLine(agentPoint, barLeftTop, 3);
		// Health bar background
		shapeBatch.setColor(Color.BLACK.cpy().add(0, 0, 0, alpha));
		shapeBatch.rectLine(barLeftTop, barRightFullTop, 6);
		shapeBatch.rectLine(barLeftBot, barRightFullBot, 6);
		// Health bar
		shapeBatch.setColor(Color.GRAY.cpy().add(0, 0, 0, alpha));
		shapeBatch.rectLine(barLeftTop, barRightTop, 6);
		shapeBatch.setColor(Color.GREEN.cpy().add(0, 0, 0, alpha));
		shapeBatch.rectLine(barLeftBot, barRightBot, 6);
		shapeBatch.end();
		// Health bars outline
		shapeBatch.begin(ShapeType.Line);
		shapeBatch.setColor(Color.BLUE.cpy().add(0, 0, 0, alpha));
		shapeBatch.rect(barLeftTop.x, barLeftTop.y + 3, barRightFullTop.x - barLeftTop.x, -12);
		shapeBatch.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		// Overwatch Icon
		if (a.getAgent().isOverwatching()) {
			shapeBatch.begin(ShapeType.Filled);
			shapeBatch.setColor(Color.GRAY);
			shapeBatch.circle(barRightFullTop.x + 8, (barRightFullTop.y + barRightFullBot.y)/2, 6);
			shapeBatch.end();
		}
	}

	@Override
	public void update() {
		super.update();
		anchorPoint.x = a.x;
		anchorPoint.y = a.y;
	}

}
