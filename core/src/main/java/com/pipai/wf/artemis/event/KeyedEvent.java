package com.pipai.wf.artemis.event;

import net.mostlyoriginal.api.event.common.Event;

public class KeyedEvent implements Event {

	private static int nextKey;

	private final int key;

	public KeyedEvent() {
		key = nextKey;
		nextKey += 1;
	}

	public int getKey() {
		return key;
	}

}
