package com.pipai.wf.gui.animation;

import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.gui.BattleGui;
import com.pipai.wf.gui.camera.CameraMovementObserver;
import com.pipai.wf.guiobject.battle.AgentGuiObject;
import com.pipai.wf.guiobject.battle.BulletGuiObject;
import com.pipai.wf.util.Alarm;

public class SuppressionAnimationHandler extends AnimationHandler implements CameraMovementObserver {

	private AgentGuiObject performer, target;
	private Alarm alarm;

	public SuppressionAnimationHandler(BattleGui gui, BattleEvent event) {
		super(gui);
		this.performer = getGui().getAgentGUIObject(event.getPerformer());
		this.target = getGui().getAgentGUIObject(event.getTarget());
		alarm = new Alarm();
	}

	@Override
	protected void initAnimation() {
		getCamera().moveTo((performer.x + target.x) / 2, (performer.y + target.y) / 2, this);
	}

	@Override
	public void notifyCameraMoveEnd() {
		alarm.set(1);
		releaseControl();
	}

	@Override
	public void notifyCameraMoveInterrupt() {
		notifyCameraMoveEnd();
	}

	@Override
	public void update() {
		alarm.update();
		if (alarm.check()) {
			fireBullet();
			alarm.set(30);
		}
	}

	private void fireBullet() {
		BulletGuiObject bullet = new BulletGuiObject(getGui(), performer.x, performer.y, target.x, target.y, target);
		getGui().createInstance(bullet);
	}

}
