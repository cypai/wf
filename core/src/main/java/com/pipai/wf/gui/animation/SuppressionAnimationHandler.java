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
		performer = getGui().getAgentGUIObject(event.getPerformer());
		target = getGui().getAgentGUIObject(event.getTarget());
		alarm = new Alarm();
	}

	@Override
	protected void initAnimation() {
		getCamera().moveTo((performer.getX() + target.getX()) / 2, (performer.getY() + target.getY()) / 2, this);
	}

	@Override
	public void notifyCameraMoveEnd() {
		alarm.setTime(1);
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
			alarm.setTime(30);
		}
	}

	private void fireBullet() {
		BulletGuiObject bullet = new BulletGuiObject(getGui(), performer.getX(), performer.getY(), target.getX(), target.getY(), target);
		getGui().createInstance(bullet);
	}

}
