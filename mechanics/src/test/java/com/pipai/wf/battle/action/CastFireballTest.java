package com.pipai.wf.battle.action;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.pipai.libgdx.test.GdxMockedTest;
import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.AccuracyPercentages;
import com.pipai.wf.battle.damage.DamageCalculator;
import com.pipai.wf.battle.damage.DamageFunction;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.event.BattleEvent;
import com.pipai.wf.battle.event.MoveEvent;
import com.pipai.wf.battle.event.OverwatchActivationEvent;
import com.pipai.wf.battle.event.OverwatchEvent;
import com.pipai.wf.battle.event.RangedSpellAttackEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.InnateCasting;
import com.pipai.wf.item.weapon.SpellWeapon;
import com.pipai.wf.spell.FireballSpell;
import com.pipai.wf.test.MockGUIObserver;
import com.pipai.wf.test.WfTestUtils;
import com.pipai.wf.unit.ability.FireActualizationAbility;
import com.pipai.wf.unit.ability.FireballAbility;
import com.pipai.wf.util.GridPosition;
import com.pipai.wf.util.UtilFunctions;

public class CastFireballTest extends GdxMockedTest {

	@Test
	public void testCastFireballLog() throws IllegalActionException {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		DamageCalculator mockDamageCalculator = Mockito.mock(DamageCalculator.class);
		Mockito.when(mockDamageCalculator.rollDamageGeneral(
				Matchers.any(AccuracyPercentages.class),
				Matchers.any(DamageFunction.class),
				Matchers.anyInt())).thenReturn(new DamageResult(true, false, 1, 0));
		Mockito.when(mockConfig.getDamageCalculator()).thenReturn(mockDamageCalculator);
		BattleMap map = new BattleMap(3, 4);
		GridPosition playerPos = new GridPosition(1, 0);
		GridPosition enemyPos = new GridPosition(2, 2);
		Agent player = WfTestUtils.createGenericAgent(Team.PLAYER, playerPos);
		SpellWeapon spellWeapon = new InnateCasting();
		player.getInventory().setItem(spellWeapon, 1);
		player.getAbilities().add(new FireActualizationAbility(1));
		player.getAbilities().add(new FireballAbility());
		map.addAgent(player);
		Agent enemy = WfTestUtils.createGenericAgent(Team.ENEMY, enemyPos);
		map.addAgent(enemy);
		BattleController controller = new BattleController(map, mockConfig);
		MockGUIObserver observer = new MockGUIObserver();
		controller.registerObserver(observer);
		new ReadySpellAction(controller, player, spellWeapon, new FireballSpell()).perform();
		Assert.assertTrue(spellWeapon.getSpell() != null);
		new TargetedSpellWeaponAction(controller, player, enemy, spellWeapon).perform();
		RangedSpellAttackEvent ev = (RangedSpellAttackEvent) observer.getEvent();
		Assert.assertEquals(player, ev.performer);
		Assert.assertEquals(enemy, ev.target);
		Assert.assertTrue(ev.spell instanceof FireballSpell);
		Assert.assertEquals(0, ev.getChainEvents().size());
		// Player has 1000 aim, cannot miss
		Assert.assertTrue(ev.damageResult.isHit());
		int expectedHP = UtilFunctions.clamp(0, enemy.getMaxHP(), enemy.getMaxHP() - ev.damageResult.getDamage());
		Assert.assertEquals(expectedHP, enemy.getHP());
		Assert.assertEquals(player.getMaxHP(), player.getHP());
		Assert.assertEquals(null, spellWeapon.getSpell());
	}

	@Test
	public void testFireballOverwatchLog() throws IllegalActionException {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		Mockito.when(mockConfig.sightRange()).thenReturn(17);
		DamageCalculator mockDamageCalculator = Mockito.mock(DamageCalculator.class);
		Mockito.when(mockDamageCalculator.rollDamageGeneral(
				Matchers.any(AccuracyPercentages.class),
				Matchers.any(DamageFunction.class),
				Matchers.anyInt())).thenReturn(new DamageResult(false, false, 0, 0));
		Mockito.when(mockConfig.getDamageCalculator()).thenReturn(mockDamageCalculator);
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
		Agent player = WfTestUtils.createGenericAgent(Team.PLAYER, playerPos);
		SpellWeapon spellWeapon = new InnateCasting();
		player.getInventory().setItem(spellWeapon, 1);
		player.getAbilities().add(new FireActualizationAbility(1));
		player.getAbilities().add(new FireballAbility());
		map.addAgent(player);
		Agent enemy = WfTestUtils.createGenericAgent(Team.ENEMY, enemyPos);
		map.addAgent(enemy);
		BattleController controller = new BattleController(map, mockConfig);
		MockGUIObserver observer = new MockGUIObserver();
		controller.registerObserver(observer);
		new ReadySpellAction(controller, player, spellWeapon, new FireballSpell()).perform();
		Assert.assertTrue(spellWeapon.getSpell() instanceof FireballSpell);
		// Hack to refresh AP
		player.setAP(2);
		new OverwatchAction(controller, player, spellWeapon,
				new TargetedSpellWeaponAction(controller, player, spellWeapon)).perform();
		OverwatchEvent ev = (OverwatchEvent) observer.getEvent();
		Assert.assertEquals(player, ev.performer);
		Assert.assertEquals("Overwatch: Fireball", ev.overwatchName);
		Assert.assertEquals(0, ev.getChainEvents().size());
		// Test Overwatch Activation
		LinkedList<GridPosition> path = new LinkedList<>();
		GridPosition dest = new GridPosition(2, 1);
		path.add(enemy.getPosition());
		path.add(dest);
		MoveAction move = new MoveAction(controller, enemy, path, 1);
		move.perform();
		MoveEvent moveEv = (MoveEvent) observer.getEvent();
		Assert.assertEquals(enemy, moveEv.performer);
		LinkedList<BattleEvent> chain = moveEv.getChainEvents();
		Assert.assertEquals(1, chain.size());
		OverwatchActivationEvent owEv = (OverwatchActivationEvent) chain.peekFirst();
		Assert.assertEquals(player, owEv.performer);
		Assert.assertEquals(enemy, owEv.target);
		Assert.assertEquals(1, owEv.getChainEvents().size());
		RangedSpellAttackEvent spellAttackEvent = (RangedSpellAttackEvent) owEv.getChainEvents().get(0);
		// Overwatch will always have a chance to miss since it clamps before applying aim penalty
		int expectedHP = UtilFunctions.clamp(0, enemy.getMaxHP(),
				enemy.getMaxHP() - spellAttackEvent.damageResult.getDamage());
		Assert.assertEquals(expectedHP, enemy.getHP());
		Assert.assertEquals(player.getMaxHP(), player.getHP());
		Assert.assertEquals(null, spellWeapon.getSpell());
	}
}
