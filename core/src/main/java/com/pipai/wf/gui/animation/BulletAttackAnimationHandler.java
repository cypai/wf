package com.pipai.wf.gui.animation;

import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.gui.BattleGui;
import com.pipai.wf.gui.camera.CameraMovementObserver;
import com.pipai.wf.guiobject.GuiObject;
import com.pipai.wf.guiobject.GuiObjectDestroyEventObserver;
import com.pipai.wf.guiobject.battle.AgentGuiObject;
import com.pipai.wf.guiobject.battle.BulletGuiObject;
import com.pipai.wf.guiobject.overlay.TemporaryText;
import com.pipai.wf.util.Alarm;

public class BulletAttackAnimationHandler extends AnimationHandler implements CameraMovementObserver, GuiObjectDestroyEventObserver {

	private AgentGuiObject performer, target;
	private BattleEvent outcome;
	private Alarm alarm;

	public BulletAttackAnimationHandler(BattleGui gui, BattleEvent outcome) {
		super(gui);
		performer = getGui().getAgentGUIObject(outcome.getPerformer());
		target = getGui().getAgentGUIObject(outcome.getTarget());
		this.outcome = outcome;
		alarm = new Alarm();
	}

	@Override
	protected void initAnimation() {
		getCamera().moveTo((performer.x + target.x) / 2, (performer.y + target.y) / 2, this);
	}

	@Override
	public void notifyCameraMoveEnd() {
		BulletGuiObject bullet = new BulletGuiObject(getGui(), performer.x, performer.y, target.x, target.y, target);
		bullet.registerDestroyEventObserver(this);
		getGui().createInstance(bullet);
	}

	@Override
	public void notifyCameraMoveInterrupt() {
		notifyCameraMoveEnd();
	}

	@Override
	public void notifyOfDestroyEvent(GuiObject obj) {
		alarm.set(60);
		TemporaryText dmgTxt;
		if (outcome.isHit()) {
			dmgTxt = new TemporaryText(getGui(), target.getPosition(), (outcome.isCrit() ? "/!\\ " : "Hit: ") + String.valueOf(outcome.getDamage()));
		} else {
			dmgTxt = new TemporaryText(getGui(), target.getPosition(), "Missed");
		}
		getGui().createInstance(dmgTxt);
		target.hit(outcome);
	}

	@Override
	public void update() {
		alarm.update();
		if (alarm.check()) {
			finish();
		}
	}

}
