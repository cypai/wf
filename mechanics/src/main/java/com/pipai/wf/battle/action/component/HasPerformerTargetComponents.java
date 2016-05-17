package com.pipai.wf.battle.action.component;

public interface HasPerformerTargetComponents extends HasPerformerComponent, HasTargetComponent {

	PerformerTargetComponentImpl getPerformerTargetComponentImpl();

	@Override
	default PerformerComponent getPerformerComponent() {
		return getPerformerTargetComponentImpl();
	}

	@Override
	default TargetComponent getTargetComponent() {
		return getPerformerTargetComponentImpl();
	}

}
