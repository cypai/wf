package com.pipai.wf.artemis.system;

import com.artemis.BaseSystem;

public abstract class ProcessOnceSystem extends BaseSystem {

	private boolean processing = true;

	@Override
	protected final boolean checkProcessing() {
		return processing;
	}

	@Override
	protected final void processSystem() {
		processOnce();
		processing = false;
	}

	protected abstract void processOnce();

}
