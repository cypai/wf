package com.pipai.wf.unit.ability;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pipai.wf.misc.Copyable;
import com.pipai.wf.misc.CopyableAsNew;
import com.pipai.wf.misc.HasDescription;
import com.pipai.wf.misc.HasName;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class Ability implements HasName, HasDescription, Copyable, CopyableAsNew {

	@Override
	@JsonIgnore
	public abstract String getName();

	@Override
	@JsonIgnore
	public abstract String getDescription();

	@Override
	public abstract Ability copyAsNew();

	@Override
	public abstract Ability copy();

}
