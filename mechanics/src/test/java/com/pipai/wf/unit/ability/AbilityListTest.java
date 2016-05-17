package com.pipai.wf.unit.ability;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.pipai.wf.spell.FireballSpell;
import com.pipai.wf.spell.Spell;
import com.pipai.wf.unit.ability.component.SpellAbilityComponent;

public class AbilityListTest {

	@Test
	public void testEmpty() {
		AbilityList alist = new AbilityList();
		Assert.assertTrue(alist.isEmpty());
		Assert.assertEquals(0, alist.size());
		Assert.assertFalse(alist.hasSpell(FireballSpell.class));
	}

	@Test
	public void testHasSpell() {
		AbilityList alist = new AbilityList();
		alist.add(new FireballAbility());
		Assert.assertEquals(1, alist.size());
		Assert.assertTrue(alist.hasSpell(FireballSpell.class));
	}

	@Test
	public void testHasSpellWithNonspell() {
		AbilityList alist = new AbilityList();
		alist.add(new FireballAbility());
		alist.add(new RegenerationAbility(1));
		Assert.assertEquals(2, alist.size());
		Assert.assertTrue(alist.hasSpell(FireballSpell.class));
	}

	@Test
	public void testForEachGetSpell() {
		ArrayList<Spell> spellList = new ArrayList<>();
		AbilityList alist = new AbilityList();
		alist.add(new FireballAbility());
		alist.add(new RegenerationAbility(1));
		for (Ability a : alist) {
			if (a instanceof SpellAbilityComponent) {
				spellList.add(((SpellAbilityComponent) a).grantedSpell());
			}
		}
		Assert.assertEquals(1, spellList.size());
		Assert.assertEquals(FireballSpell.class, spellList.get(0).getClass());
	}

}
