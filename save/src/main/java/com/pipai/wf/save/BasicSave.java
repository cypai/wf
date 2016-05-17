package com.pipai.wf.save;

import java.util.Arrays;
import java.util.List;

import com.pipai.wf.save.variables.SaveVariablesComponent;
import com.pipai.wf.save.variables.SaveVariablesComponentMixin;

public class BasicSave extends Save implements SaveVariablesComponentMixin {

	private final SaveVariablesComponent saveVariablesComponent;

	public BasicSave() {
		saveVariablesComponent = new SaveVariablesComponent();
	}

	@Override
	List<SaveComponent> components() {
		return Arrays.asList(saveVariablesComponent);
	}

	@Override
	public SaveVariablesComponent getSaveVariablesComponent() {
		return saveVariablesComponent;
	}

}
