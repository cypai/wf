package com.pipai.wf.artemis.system.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.pipai.wf.artemis.system.CameraUpdateSystem;
import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.artemis.system.SelectedUnitSystem;
import com.pipai.wf.artemis.system.UiSystem;

public class InputProcessingSystem extends NoProcessingSystem {

	private InputMultiplexer multiplexer;
	private InputMultiplexer inactiveMultiplexer;

	@Override
	protected void initialize() {
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(world.getSystem(UiSystem.class).getStage());
		multiplexer.addProcessor(new ExitInputProcessor());
		multiplexer.addProcessor(world.getSystem(SelectedUnitSystem.class));
		multiplexer.addProcessor(world.getSystem(CameraUpdateSystem.class));
		multiplexer.addProcessor(world.getSystem(RayPickingInputSystem.class));
		Gdx.input.setInputProcessor(multiplexer);
		inactiveMultiplexer = new InputMultiplexer();
		inactiveMultiplexer.addProcessor(new ExitInputProcessor());
	}

	public void activateInput() {
		Gdx.input.setInputProcessor(multiplexer);
	}

	public void deactivateInput() {
		Gdx.input.setInputProcessor(inactiveMultiplexer);
	}

}
