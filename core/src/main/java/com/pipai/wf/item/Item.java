package com.pipai.wf.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pipai.wf.battle.action.HasActions;
import com.pipai.wf.misc.Copyable;
import com.pipai.wf.misc.CopyableAsNew;
import com.pipai.wf.misc.HasDescription;
import com.pipai.wf.misc.HasName;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class Item implements HasActions, HasName, HasDescription, Copyable, CopyableAsNew {

	@Override
	@JsonIgnore
	public abstract String getName();

	@Override
	@JsonIgnore
	public abstract String getDescription();

	@Override
	public abstract Item copy();

	@Override
	public abstract Item copyAsNew();

}
