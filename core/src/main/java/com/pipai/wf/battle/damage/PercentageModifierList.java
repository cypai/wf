package com.pipai.wf.battle.damage;

import java.util.ArrayList;
import java.util.Iterator;

public class PercentageModifierList implements Iterable<PercentageModifier> {

	private ArrayList<PercentageModifier> modifierList;

	public PercentageModifierList() {
		modifierList = new ArrayList<PercentageModifier>();
	}

	public void add(PercentageModifier modifier) {
		if (modifier.modifier != 0) {
			modifierList.add(modifier);
		}
	}

	public int total() {
		int total = 0;
		for (PercentageModifier p : modifierList) {
			total += p.modifier;
		}
		return total;
	}

	@Override
	public Iterator<PercentageModifier> iterator() {
		return modifierList.iterator();
	}

}
