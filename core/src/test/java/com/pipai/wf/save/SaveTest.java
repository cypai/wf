package com.pipai.wf.save;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import com.pipai.libgdx.test.GdxMockedTest;
import com.pipai.wf.battle.armor.LeatherArmor;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.misc.BasicStats;
import com.pipai.wf.unit.ability.AbilityList;
import com.pipai.wf.unit.schema.UnitSchema;

public class SaveTest extends GdxMockedTest {

	private static int SAVE_SLOT;
	private static SaveManager MANAGER = new SaveManager();

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
		party.add(new UnitSchema(name, new BasicStats(hp, maxHP, mp, maxMP, ap, maxAP, aim, mobility, defense),
				new AbilityList(), new LeatherArmor(), new ArrayList<Weapon>(), exp, 0));
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
		Assert.assertTrue(schema.getArmor() instanceof LeatherArmor);
		Assert.assertEquals(0, schema.getWeapons().size());
		Assert.assertEquals(exp, schema.getExp());
	}

	@Test
	public void newGameTest() throws IOException, CorruptedSaveException {
		NewGameSaveGenerator generator = new NewGameSaveGenerator();
		Save save = generator.generateNewSave();
		MANAGER.save(save, SAVE_SLOT);
		Save load = MANAGER.load(SAVE_SLOT);
		ArrayList<UnitSchema> party = load.getParty();
		Assert.assertEquals(6, party.size());
		UnitSchema tidus = party.get(0);
		Assert.assertEquals(3, tidus.getAbilities().size());
		for (UnitSchema schema : party) {
			Assert.assertEquals(0, schema.getExp());
		}
	}
}
