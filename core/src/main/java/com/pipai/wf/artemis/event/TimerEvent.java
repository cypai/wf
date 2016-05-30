package com.pipai.wf.artemis.event;

import com.pipai.wf.artemis.components.TimerComponent;

import net.mostlyoriginal.api.event.common.Event;

public class TimerEvent implements Event {

	public final int key;

	public TimerEvent(TimerComponent cTimer) {
		key = cTimer.key;
	}

}
