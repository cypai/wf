package com.pipai.wf.test.battle;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.pipai.wf.battle.spell.FireballSpell;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.unit.ability.Ability;
import com.pipai.wf.unit.ability.AbilityList;
import com.pipai.wf.unit.ability.FireballAbility;
import com.pipai.wf.unit.ability.RegenerationAbility;

public class AbilityListTest {
	
	@Test
	public void testEmpty() {
		AbilityList alist = new AbilityList();
		assertTrue(alist.isEmpty());
		assertTrue(alist.size() == 0);
		assertFalse(alist.hasSpell(FireballSpell.class));
	}
	
	@Test
	public void testHasSpell() {
		AbilityList alist = new AbilityList();
		alist.add(new FireballAbility());
		assertTrue(alist.size() == 1);
		assertTrue(alist.hasSpell(FireballSpell.class));
	}
	
	@Test
	public void testHasSpellWithNonspell() {
		AbilityList alist = new AbilityList();
		alist.add(new FireballAbility());
		alist.add(new RegenerationAbility(1));
		assertTrue(alist.size() == 2);
		assertTrue(alist.hasSpell(FireballSpell.class));
	}
	
	@Test
	public void testForEachGetSpell() {
		ArrayList<Spell> spellList = new ArrayList<Spell>();
		AbilityList alist = new AbilityList();
		alist.add(new FireballAbility());
		alist.add(new RegenerationAbility(1));
		for (Ability a : alist) {
			if (a.grantsSpell()) {
				spellList.add(a.getGrantedSpell());
			}
		}
		assertTrue(spellList.size() == 1);
		assertTrue(spellList.get(0).getClass() == FireballSpell.class);
	}
	
}
