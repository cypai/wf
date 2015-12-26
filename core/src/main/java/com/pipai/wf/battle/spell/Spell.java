package com.pipai.wf.battle.spell;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pipai.wf.battle.action.TargetedActionable;
import com.pipai.wf.misc.HasDescription;
import com.pipai.wf.misc.HasName;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class Spell implements HasName, HasDescription, TargetedActionable {

	public abstract int requiredMP();

	@JsonIgnore
	public abstract int getMinDamage();

	@JsonIgnore
	public abstract int getMaxDamage();

	public abstract boolean canTargetAgent();

	public abstract boolean canOverwatch();

	public abstract SpellElement element();

	@Override
	@JsonIgnore
	public abstract String getName();

	@Override
	@JsonIgnore
	public abstract String getDescription();

}
