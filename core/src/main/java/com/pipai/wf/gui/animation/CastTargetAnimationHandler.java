package com.pipai.wf.gui.animation;

import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.gui.BattleGui;
import com.pipai.wf.gui.camera.CameraMovementObserver;
import com.pipai.wf.guiobject.GuiObject;
import com.pipai.wf.guiobject.GuiObjectDestroyEventObserver;
import com.pipai.wf.guiobject.battle.AgentGuiObject;
import com.pipai.wf.guiobject.battle.FireballGuiObject;
import com.pipai.wf.util.Alarm;

public class CastTargetAnimationHandler extends AnimationHandler implements CameraMovementObserver, GuiObjectDestroyEventObserver {

	private AgentGuiObject performer, target;
	private BattleEvent outcome;
	private Alarm alarm;

	public CastTargetAnimationHandler(BattleGui gui, BattleEvent outcome) {
		super(gui);
		this.performer = getGUI().getAgentGUIObject(outcome.getPerformer());
		this.target = getGUI().getAgentGUIObject(outcome.getTarget());
		this.outcome = outcome;
		alarm = new Alarm();
	}

	@Override
	protected void beginAnimation() {
		getCamera().moveTo((performer.x + target.x)/2, (performer.y + target.y)/2, this);
	}

	@Override
	public void notifyCameraMoveEnd() {
		FireballGuiObject bullet = new FireballGuiObject(getGUI(), performer.x, performer.y, target.x, target.y, target, outcome);
		bullet.registerDestroyEventObserver(this);
		getGUI().createInstance(bullet);
	}

	@Override
	public void notifyCameraMoveInterrupt() {
		notifyCameraMoveEnd();
	}

	@Override
	public void notifyOfDestroyEvent(GuiObject obj) {
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
