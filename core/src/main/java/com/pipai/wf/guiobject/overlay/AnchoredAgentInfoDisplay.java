package com.pipai.wf.guiobject.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
		if (a.getAgent().isKO()) {
			return;
		}
		ShapeRenderer shapeBatch = batch.getShapeRenderer();
		shapeBatch.begin(ShapeType.Filled);
		int bar_width = 40;
		Vector2 agentPoint = screenPosition;
		Vector2 barLeftTop = new Vector2(screenPosition.x + 24, screenPosition.y + 24);
		Vector2 barRightFullTop = new Vector2(screenPosition.x + 24 + bar_width, screenPosition.y + 24);
		Vector2 barRightTop = new Vector2(screenPosition.x + 24 + bar_width * ((float)a.getAgent().getArmor().getHP() / (float)a.getAgent().getArmor().maxHP()), screenPosition.y + 24);
		Vector2 barLeftBot = new Vector2(screenPosition.x + 24, screenPosition.y + 18);
		Vector2 barRightFullBot = new Vector2(screenPosition.x + 24 + bar_width, screenPosition.y + 18);
		Vector2 barRightBot = new Vector2(screenPosition.x + 24 + bar_width * ((float)a.getAgent().getHP() / (float)a.getAgent().getMaxHP()), screenPosition.y + 18);
		shapeBatch.setColor(Color.BLUE);
		shapeBatch.rectLine(agentPoint, barLeftTop, 3);
		// Health bar background
		shapeBatch.setColor(Color.BLACK);
		shapeBatch.rectLine(barLeftTop, barRightFullTop, 6);
		shapeBatch.rectLine(barLeftBot, barRightFullBot, 6);
		// Health bar
		shapeBatch.setColor(Color.GRAY);
		shapeBatch.rectLine(barLeftTop, barRightTop, 6);
		shapeBatch.setColor(Color.GREEN);
		shapeBatch.rectLine(barLeftBot, barRightBot, 6);
		shapeBatch.end();
		// Health bars outline
		shapeBatch.begin(ShapeType.Line);
		shapeBatch.setColor(Color.BLUE);
		shapeBatch.rect(barLeftTop.x, barLeftTop.y + 3, barRightFullTop.x - barLeftTop.x, -12);
		shapeBatch.end();
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