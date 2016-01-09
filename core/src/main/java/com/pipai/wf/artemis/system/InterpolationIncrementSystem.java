package com.pipai.wf.artemis.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.pipai.wf.artemis.components.InterpolationComponent;

public class InterpolationIncrementSystem extends IteratingSystem {

	private ComponentMapper<InterpolationComponent> mInterpolation;

	public InterpolationIncrementSystem() {
		super(Aspect.all(InterpolationComponent.class));
	}

	@Override
	protected void process(int entityId) {
		InterpolationComponent cInterpolation = mInterpolation.get(entityId);
		cInterpolation.t += 1;
		if (cInterpolation.t > cInterpolation.maxT) {
			mInterpolation.remove(entityId);
		}
	}

}
