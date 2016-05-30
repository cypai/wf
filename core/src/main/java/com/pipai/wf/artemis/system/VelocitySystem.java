package com.pipai.wf.artemis.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.artemis.components.VelocityComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;
import com.pipai.wf.artemis.system.battle.SelectedUnitSystem;

public class VelocitySystem extends IteratingSystem {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectedUnitSystem.class);

	private ComponentMapper<XYZPositionComponent> mXyzPosition;
	private ComponentMapper<VelocityComponent> mVelocity;

	// private NeedsUpdateSystem needsUpdateSystem;

	public VelocitySystem() {
		super(Aspect.all(XYZPositionComponent.class, VelocityComponent.class));
	}

	@Override
	protected void process(int entityId) {
		Vector3 velocity = mVelocity.get(entityId).velocity;
		if (!velocity.isZero()) {
			LOGGER.debug(velocity.toString());
			mXyzPosition.get(entityId).position.add(velocity);
		}
	}

}
