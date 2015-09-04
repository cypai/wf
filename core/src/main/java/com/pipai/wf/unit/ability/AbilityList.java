package com.pipai.wf.unit.ability;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.battle.spell.SpellElement;

public class AbilityList implements Iterable<Ability> {

	private LinkedList<Ability> list;
	private HashMap<Class<? extends Spell>, Integer> spellMap;

	public AbilityList() {
		list = new LinkedList<Ability>();
		spellMap = new HashMap<Class<? extends Spell>, Integer>();
	}

	public boolean hasSpell(Spell spell) {
		Class<? extends Spell> spellClass = spell.getClass();
		return hasSpell(spellClass);
	}

	public boolean hasSpell(Class<? extends Spell> spellClass) {
		if (spellMap.containsKey(spellClass) && spellMap.get(spellClass) > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This one also checks for ability level. If just looking for a class, use the other function
	 */
	public boolean hasAbility(Ability ability) {
		return list.contains(ability);
	}

	public boolean hasAbility(Class<? extends Ability> abilityClass) {
		for (Ability a : list) {
			if (abilityClass.isInstance(a)) {
				return true;
			}
		}
		return false;
	}

	public int getActualizationLevel(SpellElement e) {
		for (Ability a : list) {
			switch (e) {
			case FIRE:
				if (a instanceof FireActualizationAbility) {
					return a.getLevel();
				}
			case WATER:
				if (a instanceof WaterActualizationAbility) {
					return a.getLevel();
				}
			case EARTH:
				if (a instanceof EarthActualizationAbility) {
					return a.getLevel();
				}
			case ELECTRIC:
				if (a instanceof ElectricActualizationAbility) {
					return a.getLevel();
				}
			case LIGHT:
				if (a instanceof LightActualizationAbility) {
					return a.getLevel();
				}
			case FORCE:
				if (a instanceof ForceActualizationAbility) {
					return a.getLevel();
				}
			default:
				break;
			}
		}
		return 0;
	}

	public boolean add(Ability arg0) {
		list.add(arg0);
		if (arg0 instanceof SpellAbility) {
			spellMap.put(arg0.getGrantedSpell().getClass(), 1);
		}
		return true;
	}

	public void add(AbilityList alist) {
		for (Ability a : alist) {
			add(a.clone());
		}
	}

	public void clear() {
		list = new LinkedList<Ability>();
		spellMap = new HashMap<Class<? extends Spell>, Integer>();
	}

	@Override
	public AbilityList clone() {
		AbilityList alist = new AbilityList();
		for (Ability a : list) {
			alist.add(a.clone());
		}
		return alist;
	}

	public boolean contains(Ability arg0) {
		return list.contains(arg0);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public boolean remove(Ability arg0) {
		list.remove(arg0);
		spellMap.remove(arg0.getGrantedSpell().getClass());
		return true;
	}

	public int size() {
		return list.size();
	}

	public static class AbilityListIterator implements Iterator<Ability> {

		Iterator<Ability> it;

		public AbilityListIterator(LinkedList<Ability> list) {
			it = list.listIterator();
		}

		@Override
		public boolean hasNext() {
			return it.hasNext();
		}

		@Override
		public Ability next() {
			return it.next();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	@Override
	public Iterator<Ability> iterator() {
		return new AbilityListIterator(list);
	}

}
