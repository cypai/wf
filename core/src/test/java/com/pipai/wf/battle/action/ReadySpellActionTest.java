package com.pipai.wf.battle.action;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.InnateCasting;
import com.pipai.wf.item.weapon.SpellWeapon;
import com.pipai.wf.spell.FireballSpell;
import com.pipai.wf.test.MockGUIObserver;
import com.pipai.wf.test.WfTestUtils;
import com.pipai.wf.unit.ability.FireActualizationAbility;
import com.pipai.wf.unit.ability.FireballAbility;

public class ReadySpellActionTest {

	@Test
	public void testReadyWithoutActualizationAbility() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		GridPosition mockPos = Mockito.mock(GridPosition.class);
		BattleController controller = new BattleController(Mockito.mock(BattleMap.class), mockConfig);
		Agent player = WfTestUtils.createGenericAgent(Team.PLAYER, mockPos);
		player.getAbilities().add(new FireballAbility());
		SpellWeapon casting = new InnateCasting();
		player.getInventory().setItem(casting, 1);
		ReadySpellAction ready = new ReadySpellAction(controller, player, casting, new FireballSpell());
		try {
			ready.perform();
			Assert.fail("Did not throw expected IllegalActionException");
		} catch (IllegalActionException e) {
			// Expected, don't do anything
		}
	}

	@Test
	public void testReadyWithoutFireballAbility() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		GridPosition mockPos = Mockito.mock(GridPosition.class);
		BattleController controller = new BattleController(Mockito.mock(BattleMap.class), mockConfig);
		Agent player = WfTestUtils.createGenericAgent(Team.PLAYER, mockPos);
		player.getAbilities().add(new FireActualizationAbility(1));
		SpellWeapon casting = new InnateCasting();
		player.getInventory().setItem(casting, 1);
		ReadySpellAction ready = new ReadySpellAction(controller, player, casting, new FireballSpell());
		try {
			ready.perform();
			Assert.fail("Did not throw expected IllegalActionException");
		} catch (IllegalActionException e) {
			// Expected, don't do anything
		}
	}

	@Test
	public void testReadyFireballLog() throws IllegalActionException {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		BattleMap map = new BattleMap(3, 4);
		GridPosition playerPos = new GridPosition(1, 0);
		Agent player = WfTestUtils.createGenericAgent(Team.PLAYER, playerPos);
		SpellWeapon casting = new InnateCasting();
		player.getInventory().setItem(casting, 1);
		player.getAbilities().add(new FireActualizationAbility(1));
		player.getAbilities().add(new FireballAbility());
		map.addAgent(player);
		BattleController controller = new BattleController(map, mockConfig);
		MockGUIObserver observer = new MockGUIObserver();
		controller.registerObserver(observer);
		new ReadySpellAction(controller, player, casting, new FireballSpell()).perform();
		BattleEvent ev = observer.getEvent();
		Assert.assertEquals(BattleEvent.Type.READY, ev.getType());
		Assert.assertEquals(player, ev.getPerformer());
		Assert.assertTrue(ev.getSpell() instanceof FireballSpell);
		Assert.assertFalse(ev.getQuickened());
		Assert.assertEquals(1, player.getAP());
		Assert.assertEquals(0, ev.getChainEvents().size());
	}

	@Test
	public void testQuickenedFireballLog() throws IllegalActionException {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		BattleMap map = new BattleMap(3, 4);
		GridPosition playerPos = new GridPosition(1, 0);
		Agent player = WfTestUtils.createGenericAgent(Team.PLAYER, playerPos);
		SpellWeapon casting = new InnateCasting();
		player.getInventory().setItem(casting, 1);
		player.getAbilities().add(new FireActualizationAbility(2));
		player.getAbilities().add(new FireballAbility());
		map.addAgent(player);
		BattleController controller = new BattleController(map, mockConfig);
		MockGUIObserver observer = new MockGUIObserver();
		controller.registerObserver(observer);
		new ReadySpellAction(controller, player, casting, new FireballSpell()).perform();
		BattleEvent ev = observer.getEvent();
		Assert.assertEquals(BattleEvent.Type.READY, ev.getType());
		Assert.assertEquals(player, ev.getPerformer());
		Assert.assertTrue(ev.getSpell() instanceof FireballSpell);
		Assert.assertTrue(ev.getQuickened());
		Assert.assertEquals(2, player.getAP());
		Assert.assertEquals(0, ev.getChainEvents().size());
	}

}
