package com.pipai.wf.artemis.system.rendering;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;
import com.pipai.wf.artemis.components.PerspectiveCameraComponent;
import com.pipai.wf.gui.BatchHelper;

public class BatchRenderingSystem extends IteratingSystem {

	private BatchHelper batch;

	public BatchRenderingSystem(BatchHelper batch) {
		super(Aspect.all(PerspectiveCameraComponent.class));
		this.batch = batch;
	}

	@Override
	protected void process(int entityId) {
		batch.getDecalBatch().flush();
		batch.getModelBatch().flush();
	}

	protected BatchHelper getBatch() {
		return batch;
	}

}
