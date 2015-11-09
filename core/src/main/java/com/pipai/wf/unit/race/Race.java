package com.pipai.wf.unit.race;

public enum Race {

	HUMAN(7, 5, 2, 12, 65, 0, "Human"), FAIRY(4, 10, 2, 13, 65, 0, "Fairy"), CAT(4, 3, 3, 14, 65, 5, "Cat"), FOX(5, 10, 2, 13, 65, 0, "Fox");

	public final int hp, mp, ap, mobility, aim, defense;
	public final String name;

	private Race(int hp, int mp, int ap, int mobility, int aim, int defense, String name) {
		this.hp = hp;
		this.mp = mp;
		this.ap = ap;
		this.mobility = mobility;
		this.aim = aim;
		this.defense = defense;
		this.name = name;
	}

}
