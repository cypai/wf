package com.pipai.wf.battle.effect;

import java.util.ArrayList;
import java.util.Iterator;

public class StatusEffectList implements Iterable<StatusEffect> {

	private ArrayList<StatusEffect> list;

	public StatusEffectList() {
		list = new ArrayList<StatusEffect>();
	}

	public void add(StatusEffect newEffect) {
		// If we add the same effect, replace the old one with the new one
		StatusEffect identical = null;	// If list already has it then store the identical version here
		for (StatusEffect se : list) {
			if (se.getClass().isInstance(newEffect)) {
				identical = se;
				break;
			}
		}
		if (identical != null) {
			// If the old version will last longer, keep it and return;
			if (identical.getCooldown() >= newEffect.getCooldown()) {
				return;
			}
			list.remove(identical);
		}
		list.add(newEffect);
	}

	public void clear() {
		list.clear();
	}

	public int size() {
		return list.size();
	}

	public int totalAimModifier() {
		int total = 0;
		for (StatusEffect se : list) {
			total += se.flatAimModifier();
		}
		return total;
	}

	public int totalMobilityModifier() {
		int total = 0;
		for (StatusEffect se : list) {
			total += se.flatMobilityModifier();
		}
		return total;
	}

	@Override
	public Iterator<StatusEffect> iterator() {
		return list.iterator();
	}

}
