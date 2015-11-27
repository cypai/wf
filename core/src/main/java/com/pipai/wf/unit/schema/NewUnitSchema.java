package com.pipai.wf.unit.schema;

import com.pipai.wf.misc.BasicStats;

public abstract class NewUnitSchema extends UnitSchema {

	public NewUnitSchema(String name, int hp, int mp, int ap, int aim, int mobility, int defense) {
		super(name, new BasicStats(hp, hp, mp, mp, ap, ap, aim, mobility, defense));
	}

	@Override
	public final int getHP() {
		return getMaxHP();
	}

	@Override
	public final int getMP() {
		return getMaxMP();
	}

}
