package com.pipai.wf.artemis.system;

import java.util.ArrayList;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.pipai.wf.artemis.components.NeedsUpdateComponent;

public class NeedsUpdateSystem extends BaseEntitySystem {

	private ComponentMapper<NeedsUpdateComponent> mNeedsUpdate;
	private ArrayList<Entity> buffer;

	private int needsActive;

	public NeedsUpdateSystem() {
		super(Aspect.all(NeedsUpdateComponent.class));
		buffer = new ArrayList<>();
		needsActive = 2;
	}

	@Override
	protected void inserted(int entityId) {
		activate();
	}

	/**
	 * Notify the system that this entity needs to be updated on next iteration
	 */
	public void notify(int entityId) {
		buffer.add(world.getEntity(entityId));
		activate();
	}

	/**
	 * Notify the system that this entity needs to be updated on next iteration
	 */
	public void notify(Entity entity) {
		buffer.add(entity);
		activate();
	}

	private void activate() {
		needsActive = 2;
	}

	@Override
	protected boolean checkProcessing() {
		return needsActive > 0;
	}

	@Override
	protected void processSystem() {
		if (checkProcessing()) {
			for (int id : subscription.getEntities().getData()) {
				mNeedsUpdate.remove(id);
			}
			for (Entity e : buffer) {
				mNeedsUpdate.create(e);
			}
			buffer.clear();
			needsActive -= 1;
		}
	}

}
