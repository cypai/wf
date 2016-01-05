package com.pipai.wf.battle.action.component;

public interface DefaultApRequiredComponent extends ApRequiredComponent {

	@Override
	default int getAPRequired() {
		return 1;
	}

}
