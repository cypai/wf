package com.pipai.wf.artemis.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.pipai.wf.artemis.components.TimedDestroyComponent;

public class TimedDestroySystem extends IteratingSystem {

	private ComponentMapper<TimedDestroyComponent> mTimedDestroy;

	public TimedDestroySystem() {
		super(Aspect.all(TimedDestroyComponent.class));
	}

	@Override
	protected void process(int entityId) {
		TimedDestroyComponent cTimedDestroy = mTimedDestroy.get(entityId);
		cTimedDestroy.time -= 1;
		if (cTimedDestroy.time <= 0) {
			world.delete(entityId);
		}
	}

}
