package com.pipai.wf.misc;

public class BasicStats {

	private int hp, maxHP, mp, maxMP, ap, maxAP, aim, mobility, defense;

	public BasicStats(int hp, int maxHP, int mp, int maxMP, int ap, int maxAP, int aim, int mobility, int defense) {
		this.hp = hp;
		this.maxHP = maxHP;
		this.mp = mp;
		this.maxMP = maxMP;
		this.ap = ap;
		this.maxAP = maxAP;
		this.aim = aim;
		this.mobility = mobility;
		this.defense = defense;
	}

	/**
	 * Constructor that sets each stat to its maximum
	 */
	public BasicStats(int maxHP, int maxMP, int maxAP, int aim, int mobility, int defense) {
		hp = maxHP;
		this.maxHP = maxHP;
		mp = maxMP;
		this.maxMP = maxMP;
		ap = maxAP;
		this.maxAP = maxAP;
		this.aim = aim;
		this.mobility = mobility;
		this.defense = defense;
	}

	public BasicStats() {
		// Empty constructor for Jackson
	}

	@Override
	public BasicStats clone() {
		return new BasicStats(hp, maxHP, mp, maxMP, ap, maxAP, aim, mobility, defense);
	}

	public int getHP() {
		return hp;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public int getMP() {
		return mp;
	}

	public int getMaxMP() {
		return maxMP;
	}

	public int getAP() {
		return ap;
	}

	public int getMaxAP() {
		return maxAP;
	}

	public int getAim() {
		return aim;
	}

	public int getMobility() {
		return mobility;
	}

	public int getDefense() {
		return defense;
	}

	public void setHP(int hp) {
		this.hp = hp;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public void setMP(int mp) {
		this.mp = mp;
	}

	public void setMaxMP(int maxMP) {
		this.maxMP = maxMP;
	}

	public void setAP(int ap) {
		this.ap = ap;
	}

	public void setMaxAP(int maxAP) {
		this.maxAP = maxAP;
	}

	public void setAim(int aim) {
		this.aim = aim;
	}

	public void setMobility(int mobility) {
		this.mobility = mobility;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

}
