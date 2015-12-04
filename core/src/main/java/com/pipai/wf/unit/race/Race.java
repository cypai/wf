package com.pipai.wf.unit.race;

import com.pipai.wf.misc.BasicStats;
import com.pipai.wf.misc.HasBasicStats;

public enum Race implements HasBasicStats {

	HUMAN(7, 5, 2, 65, 12, 0, "Human"), FAIRY(4, 10, 2, 65, 13, 0, "Fairy"), CAT(4, 3, 3, 65, 14, 5, "Cat"), FOX(5, 10, 2, 65, 13, 0, "Fox");

	private final BasicStats basicStats;
	private final String name;

	Race(int hp, int mp, int ap, int aim, int mobility, int defense, String name) {
		basicStats = new BasicStats(hp, hp, mp, mp, ap, ap, aim, mobility, defense);
		this.name = name;
	}

	@Override
	public BasicStats getBasicStats() {
		return basicStats;
	}

	public String getName() {
		return name;
	}

}
