package com.pipai.wf.artemis.system;

import com.artemis.BaseSystem;
import com.pipai.wf.gui.BatchHelper;

public class BatchRenderingSystem extends BaseSystem {

	private BatchHelper batch;

	public BatchRenderingSystem(BatchHelper batch) {
		this.batch = batch;
	}

	@Override
	protected void processSystem() {
		batch.getDecalBatch().flush();
		batch.getModelBatch().flush();
	}

}
