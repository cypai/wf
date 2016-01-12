package com.pipai.wf.artemis.event;

import net.mostlyoriginal.api.event.common.Event;

public class LeftClickEvent implements Event {

	public int clickedEntityId;

	public LeftClickEvent(int clickedEntityId) {
		this.clickedEntityId = clickedEntityId;
	}

}
