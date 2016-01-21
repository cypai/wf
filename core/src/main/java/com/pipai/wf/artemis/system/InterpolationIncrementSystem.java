package com.pipai.wf.artemis.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.pipai.wf.artemis.components.InterpolationComponent;

import net.mostlyoriginal.api.event.common.EventSystem;

public class InterpolationIncrementSystem extends IteratingSystem {

	private ComponentMapper<InterpolationComponent> mInterpolation;

	private EventSystem eventSystem;

	public InterpolationIncrementSystem() {
		super(Aspect.all(InterpolationComponent.class));
	}

	@Override
	protected void process(int entityId) {
		InterpolationComponent cInterpolation = mInterpolation.get(entityId);
		cInterpolation.t += 1;
		if (cInterpolation.t > cInterpolation.maxT) {
			mInterpolation.remove(entityId);
			if (cInterpolation.onEndEvent != null) {
				cInterpolation.onEndEvent.entityId = entityId;
				eventSystem.dispatch(cInterpolation.onEndEvent);
			}
		}
	}

}
