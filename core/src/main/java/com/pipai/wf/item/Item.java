package com.pipai.wf.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pipai.wf.misc.HasDescription;
import com.pipai.wf.misc.HasName;

public abstract class Item implements HasName, HasDescription {

	@Override
	@JsonIgnore
	public abstract String getName();

	@Override
	@JsonIgnore
	public abstract String getDescription();

}
