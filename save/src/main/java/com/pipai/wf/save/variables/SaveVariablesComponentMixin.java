package com.pipai.wf.save.variables;

import java.util.HashMap;
import java.util.Optional;

public interface SaveVariablesComponentMixin {

	SaveVariablesComponent getSaveVariablesComponent();

	default Optional<String> getVariable(String name) {
		return getSaveVariablesComponent().getVariable(name);
	}

	default void setVariable(String name, String value) {
		getSaveVariablesComponent().setVariable(name, value);
	}

	default HashMap<String, String> getVariables() {
		return getSaveVariablesComponent().getVariables();
	}

}
