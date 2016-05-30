package com.pipai.wf.artemis.components;

import com.artemis.Component;

public class TimerComponent extends Component {

	public final int key;

	public int timer;

	// Set to 1 to avoid potential collisions with default key initialization of 0
	private static int nextKey = 1;

	public TimerComponent() {
		key = nextKey;
		nextKey += 1;
	}

}
