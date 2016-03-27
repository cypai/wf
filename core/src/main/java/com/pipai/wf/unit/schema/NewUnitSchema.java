package com.pipai.wf.unit.schema;

import com.pipai.wf.battle.inventory.AgentInventory;
import com.pipai.wf.misc.BasicStats;
import com.pipai.wf.unit.ability.AbilityList;

public abstract class NewUnitSchema extends ImmutableUnitSchema {

	public NewUnitSchema(String name, BasicStats stats) {
		super(name, stats);
	}

	@Override
	public final int getHP() {
		return getMaxHP();
	}

	@Override
	public final int getMP() {
		return getMaxMP();
	}

	@Override
	public int getExp() {
		return 0;
	}

	@Override
	public abstract int getExpGiven();

	@Override
	public abstract AbilityList getAbilities();

	@Override
	public abstract AgentInventory getInventory();

	@Override
	public abstract int getLevel();

}
