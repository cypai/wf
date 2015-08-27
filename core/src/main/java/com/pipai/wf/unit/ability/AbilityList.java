package com.pipai.wf.unit.ability;

import java.util.HashMap;
import java.util.LinkedList;

import com.pipai.wf.battle.spell.Spell;

public class AbilityList {
	
	private LinkedList<Ability> list;
	private HashMap<Spell, Integer> spellMap;
	
	public AbilityList() {
		list = new LinkedList<Ability>();
		spellMap = new HashMap<Spell, Integer>();
	}
	
	public boolean add(Ability arg0) {
		list.add(arg0);
		if (arg0 instanceof SpellAbility) {
			spellMap.put(arg0.grantsSpell(), 1);
		}
		return true;
	}
	
	public void clear() {
		list = new LinkedList<Ability>();
		spellMap = new HashMap<Spell, Integer>();
	}
	
	public boolean contains(Ability arg0) {
		return list.contains(arg0);
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	public boolean remove(Ability arg0) {
		list.remove(arg0);
		spellMap.remove(arg0.grantsSpell());
		return true;
	}
	
	public int size() {
		return list.size();
	}
	
}
