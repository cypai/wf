package com.pipai.wf.artemis.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.managers.TagManager;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.pipai.wf.artemis.components.PerspectiveCameraComponent;
import com.pipai.wf.gui.BatchHelper;

public class BatchRenderingSystem extends IteratingSystem {

	private ComponentMapper<PerspectiveCameraComponent> mPerspectiveCamera;

	private TagManager tagManager;

	private boolean hasCamera;

	private PerspectiveCamera camera;
	private BatchHelper batch;

	public BatchRenderingSystem(BatchHelper batch) {
		super(Aspect.all(PerspectiveCameraComponent.class));
		this.batch = batch;
		hasCamera = false;
	}

	@Override
	protected void process(int entityId) {
		if (!hasCamera) {
			camera = mPerspectiveCamera.get(tagManager.getEntity(Tag.CAMERA.toString())).camera;
			batch.set3DCamera(camera);
			hasCamera = true;
		}
		batch.getDecalBatch().flush();
		batch.getModelBatch().flush();
	}

	protected BatchHelper getBatch() {
		return batch;
	}

}
