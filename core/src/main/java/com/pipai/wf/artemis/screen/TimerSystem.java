package com.pipai.wf.artemis.screen;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.pipai.wf.artemis.components.TimerComponent;
import com.pipai.wf.artemis.event.TimerEvent;

import net.mostlyoriginal.api.event.common.EventSystem;

public class TimerSystem extends IteratingSystem {

	private ComponentMapper<TimerComponent> mTimer;

	private EventSystem eventSystem;

	public TimerSystem() {
		super(Aspect.all(TimerComponent.class));
	}

	@Override
	protected void process(int entityId) {
		TimerComponent cTimer = mTimer.get(entityId);
		if (cTimer.timer > 0) {
			cTimer.timer -= 1;
		} else {
			eventSystem.dispatch(new TimerEvent(cTimer));
			mTimer.remove(entityId);
		}
	}

}
