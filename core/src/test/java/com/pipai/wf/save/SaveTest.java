package com.pipai.wf.save;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import com.pipai.libgdx.test.GdxMockedTest;
import com.pipai.wf.battle.inventory.AgentInventory;
import com.pipai.wf.item.armor.LeatherArmor;
import com.pipai.wf.item.weapon.Pistol;
import com.pipai.wf.item.weapon.Weapon;
import com.pipai.wf.misc.BasicStats;
import com.pipai.wf.unit.ability.AbilityList;
import com.pipai.wf.unit.schema.UnitSchema;

public class SaveTest extends GdxMockedTest {

	private static final int SAVE_SLOT = 0;
	private static final SaveManager MANAGER = new SaveManager();

	@After
	public void tearDown() {
		MANAGER.delete(SAVE_SLOT);
	}

	@AfterClass
	public static void clean() {
		File saveDir = new File(SaveManager.DEFAULT_DIRECTORY);
		saveDir.delete();
	}

	@Test
	public void smallPartyTest() throws IOException, CorruptedSaveException {
		String name = "test name";
		int hp = 4;
		int maxHP = 5;
		int mp = 6;
		int maxMP = 7;
		int ap = 1;
		int maxAP = 2;
		int aim = 60;
		int mobility = 10;
		int defense = 0;
		int exp = 10;
		Save save = new Save();
		ArrayList<UnitSchema> party = new ArrayList<>();
		List<Weapon> weapons = new ArrayList<>();
		weapons.add(new Pistol());
		AgentInventory inventory = new AgentInventory(3);
		inventory.setItem(new LeatherArmor(), 1);
		inventory.setItem(new Pistol(), 2);
		party.add(new UnitSchema(name, new BasicStats(hp, maxHP, mp, maxMP, ap, maxAP, aim, mobility, defense),
				new AbilityList(), inventory, 1, exp, 0));
		save.setParty(party);
		MANAGER.save(save, SAVE_SLOT);
		Save load;
		load = MANAGER.load(SAVE_SLOT);
		UnitSchema schema = load.getParty().get(0);
		Assert.assertEquals(name, schema.getName());
		Assert.assertEquals(hp, schema.getHP());
		Assert.assertEquals(maxHP, schema.getMaxHP());
		Assert.assertEquals(mp, schema.getMP());
		Assert.assertEquals(maxMP, schema.getMaxMP());
		Assert.assertEquals(ap, schema.getAP());
		Assert.assertEquals(maxAP, schema.getMaxAP());
		Assert.assertEquals(aim, schema.getAim());
		Assert.assertEquals(mobility, schema.getMobility());
		Assert.assertEquals(defense, schema.getDefense());
		Assert.assertEquals(0, schema.getAbilities().size());
		Assert.assertTrue(schema.getInventory().getItem(1) instanceof LeatherArmor);
		Assert.assertEquals(1, schema.getLevel());
		Assert.assertEquals(exp, schema.getExp());
		Assert.assertTrue(schema.getInventory().getItem(2) instanceof Pistol);
	}

	@Test
	public void newGameTest() throws IOException, CorruptedSaveException {
		NewGameSaveGenerator generator = new NewGameSaveGenerator();
		Save save = generator.generateNewSave();
		MANAGER.save(save, SAVE_SLOT);
		Save load = MANAGER.load(SAVE_SLOT);
		List<UnitSchema> party = load.getParty();
		Assert.assertEquals(6, party.size());
		UnitSchema tidus = party.get(0);
		Assert.assertEquals(3, tidus.getAbilities().size());
		for (UnitSchema schema : party) {
			Assert.assertEquals(0, schema.getExp());
		}
	}
}
