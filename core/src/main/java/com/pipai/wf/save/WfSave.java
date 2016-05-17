package com.pipai.wf.save;

import java.util.ArrayList;
import java.util.List;

import com.pipai.wf.unit.schema.MutableUnitSchema;
import com.pipai.wf.unit.schema.UnitSchema;

public class WfSave extends BasicSave {

	private final WfPartySaveComponent partyComponent;

	public WfSave() {
		partyComponent = new WfPartySaveComponent();
	}

	@Override
	List<SaveComponent> components() {
		List<SaveComponent> components = new ArrayList<>();
		components.addAll(super.components());
		components.add(partyComponent);
		return components;
	}

	public List<MutableUnitSchema> getParty() {
		return partyComponent.getParty();
	}

	public void setParty(List<? extends UnitSchema> party) {
		partyComponent.setParty(party);
	}

}
