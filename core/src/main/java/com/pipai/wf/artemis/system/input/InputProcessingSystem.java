package com.pipai.wf.artemis.system.input;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.pipai.wf.artemis.system.CameraUpdateSystem;
import com.pipai.wf.artemis.system.SelectedUnitSystem;

public class InputProcessingSystem extends BaseSystem {

	private InputMultiplexer multiplexer;

	@Override
	protected void initialize() {
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new ExitInputProcessor());
		multiplexer.addProcessor(world.getSystem(SelectedUnitSystem.class));
		multiplexer.addProcessor(world.getSystem(CameraUpdateSystem.class));
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	protected boolean checkProcessing() {
		return false;
	}

	@Override
	protected void processSystem() {
		// Do nothing
	}

}
