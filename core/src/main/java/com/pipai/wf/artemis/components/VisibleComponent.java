package com.pipai.wf.artemis.components;

import com.artemis.Component;

public class VisibleComponent extends Component {

	public boolean visible;

	public VisibleComponent() {
		visible = true;
	}

	public VisibleComponent(boolean visible) {
		this.visible = visible;
	}

}
