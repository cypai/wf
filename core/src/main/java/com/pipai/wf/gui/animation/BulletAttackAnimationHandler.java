package com.pipai.wf.gui.animation;

import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.gui.BattleGUI;
import com.pipai.wf.gui.camera.CameraMovementObserver;
import com.pipai.wf.guiobject.GUIObject;
import com.pipai.wf.guiobject.GUIObjectDestroyEventObserver;
import com.pipai.wf.guiobject.battle.AgentGUIObject;
import com.pipai.wf.guiobject.battle.BulletGUIObject;
import com.pipai.wf.util.Alarm;

public class BulletAttackAnimationHandler extends AnimationHandler implements CameraMovementObserver, GUIObjectDestroyEventObserver {

	private AgentGUIObject performer, target;
	private BattleEvent outcome;
	private Alarm alarm;

	public BulletAttackAnimationHandler(BattleGUI gui, AgentGUIObject performer, AgentGUIObject target, BattleEvent outcome) {
		super(gui);
		this.performer = performer;
		this.target = target;
		this.outcome = outcome;
		alarm = new Alarm();
	}

	@Override
	protected void beginAnimation() {
		getCamera().moveTo((performer.x + target.x)/2, (performer.y + target.y)/2, this);
	}

	@Override
	public void notifyCameraMoveEnd() {
		BulletGUIObject bullet = new BulletGUIObject(getGUI(), performer.x, performer.y, target.x, target.y, target, outcome);
		bullet.registerDestroyEventObserver(this);
		getGUI().createInstance(bullet);
	}

	@Override
	public void notifyCameraMoveInterrupt() {
		notifyCameraMoveEnd();
	}

	@Override
	public void notifyOfDestroyEvent(GUIObject obj) {
		alarm.set(60);
	}

	@Override
	public void update() {
		alarm.update();
		if (alarm.check()) {
			finish();
		}
	}

}
