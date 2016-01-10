package com.pipai.wf.artemis.components;

import com.artemis.Component;

public class PlayerUnitComponent extends Component {

	private static int nextIndex;

	public final int index;

	public PlayerUnitComponent() {
		index = nextIndex;
		nextIndex += 1;
	}

}
