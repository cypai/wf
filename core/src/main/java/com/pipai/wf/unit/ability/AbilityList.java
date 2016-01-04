package com.pipai.wf.unit.ability;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.exception.NoRegisteredAgentException;
import com.pipai.wf.spell.Spell;
import com.pipai.wf.spell.SpellElement;
import com.pipai.wf.unit.ability.component.RoundEndComponent;
import com.pipai.wf.unit.ability.component.SpellAbilityComponent;

public class AbilityList implements Iterable<Ability> {

	private Agent registeredAgent;
	private LinkedList<Ability> abilityList;
	private HashMap<Class<? extends Spell>, Integer> spellMap;

	public AbilityList() {
		abilityList = new LinkedList<Ability>();
		spellMap = new HashMap<Class<? extends Spell>, Integer>();
	}

	public void registerToAgent(Agent agent) {
		registeredAgent = agent;
	}

	public List<Ability> getAbilityList() {
		return abilityList;
	}

	public boolean hasSpell(Spell spell) {
		Class<? extends Spell> spellClass = spell.getClass();
		return hasSpell(spellClass);
	}

	public boolean hasSpell(Class<? extends Spell> spellClass) {
		return spellMap.containsKey(spellClass) && spellMap.get(spellClass) > 0;
	}

	/**
	 * This one also checks for ability level. If just looking for a class, use the other function
	 */
	public boolean hasAbility(Ability ability) {
		return abilityList.contains(ability);
	}

	public boolean hasAbility(Class<? extends Ability> abilityClass) {
		for (Ability a : abilityList) {
			if (abilityClass.isInstance(a)) {
				return true;
			}
		}
		return false;
	}

	public Ability getAbility(Class<? extends Ability> abilityClass) {
		for (Ability a : abilityList) {
			if (abilityClass.isInstance(a)) {
				return a;
			}
		}
		return null;
	}

	public int getActualizationLevel(SpellElement e) {
		for (Ability a : abilityList) {
			switch (e) {
			case FIRE:
				if (a instanceof FireActualizationAbility) {
					return ((FireActualizationAbility) a).getLevel();
				}
				break;
			default:
				break;
			}
		}
		return 0;
	}

	public void onRoundEnd() throws NoRegisteredAgentException {
		for (Ability a : abilityList) {
			if (a instanceof RoundEndComponent) {
				((RoundEndComponent) a).onRoundEnd(registeredAgent);
			}
		}
	}

	public boolean add(Ability arg0) {
		abilityList.add(arg0);
		if (arg0 instanceof SpellAbilityComponent) {
			spellMap.put(((SpellAbilityComponent) arg0).grantedSpell().getClass(), 1);
		}
		return true;
	}

	public void add(AbilityList alist) {
		for (Ability a : alist) {
			add(a.clone());
		}
	}

	public void clear() {
		abilityList = new LinkedList<Ability>();
		spellMap = new HashMap<Class<? extends Spell>, Integer>();
	}

	@Override
	public AbilityList clone() {
		AbilityList alist = new AbilityList();
		for (Ability a : abilityList) {
			alist.add(a.clone());
		}
		return alist;
	}

	public boolean contains(Ability arg0) {
		return abilityList.contains(arg0);
	}

	@JsonIgnore
	public boolean isEmpty() {
		return abilityList.isEmpty();
	}

	public boolean remove(Ability arg0) {
		abilityList.remove(arg0);
		if (arg0 instanceof SpellAbilityComponent) {
			spellMap.remove(((SpellAbilityComponent) arg0).grantedSpell().getClass());
		}
		return true;
	}

	public int size() {
		return abilityList.size();
	}

	@Override
	public Iterator<Ability> iterator() {
		return new AbilityListIterator(abilityList);
	}

	public static class AbilityListIterator implements Iterator<Ability> {

		private Iterator<Ability> it;

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

}
