package com.pipai.wf.unit.abilitytree;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.unit.ability.Ability;

public class AbilityTreeTest {

	@Test
	public void testRootHeight() {
		Ability mock = Mockito.mock(Ability.class);
		AbilityTree tree = new AbilityTree.Builder(mock).build();
		Assert.assertEquals(1, tree.getHeight());
	}

	@Test
	public void testHeight() {
		Ability mock = Mockito.mock(Ability.class);
		AbilityTree tree = new AbilityTree.Builder(mock)
				.addAbilityAtCurrentLevel(mock)
				.addAbilityAtCurrentLevel(mock)
				.goToNextLevel()
				.addAbilityAtCurrentLevel(mock)
				.build();
		Assert.assertEquals(3, tree.getHeight());
	}

	@Test
	public void testLevelAmount() {
		Ability mock = Mockito.mock(Ability.class);
		AbilityTree tree = new AbilityTree.Builder(mock)
				.addAbilityAtCurrentLevel(mock)
				.addAbilityAtCurrentLevel(mock)
				.goToNextLevel()
				.addAbilityAtCurrentLevel(mock)
				.addAbilityAtCurrentLevel(mock)
				.addAbilityAtCurrentLevel(mock)
				.build();
		Assert.assertEquals(1, tree.getAbilitiesAtHeight(1).size());
		Assert.assertEquals(2, tree.getAbilitiesAtHeight(2).size());
		Assert.assertEquals(3, tree.getAbilitiesAtHeight(3).size());
	}

	@Test
	public void testHighestTaken() {
		Ability mock = Mockito.mock(Ability.class);
		AbilityTree tree = new AbilityTree.Builder(mock)
				.addAbilityAtCurrentLevel(mock)
				.addAbilityAtCurrentLevel(mock)
				.goToNextLevel()
				.addAbilityAtCurrentLevel(mock)
				.build();
		tree.getAbilitiesAtHeight(1).get(0).setTaken(true);
		Assert.assertEquals(1, tree.getHighestTakenLevel());
		tree.getAbilitiesAtHeight(2).get(0).setTaken(true);
		Assert.assertEquals(2, tree.getHighestTakenLevel());
		tree.getAbilitiesAtHeight(3).get(0).setTaken(true);
		Assert.assertEquals(3, tree.getHighestTakenLevel());
	}

}
