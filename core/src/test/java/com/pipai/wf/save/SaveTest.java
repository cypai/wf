package com.pipai.wf.save;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import com.badlogic.gdx.files.FileHandle;
import com.pipai.libgdx.test.GdxMockedTest;
import com.pipai.wf.battle.inventory.AgentInventory;
import com.pipai.wf.item.armor.LeatherArmor;
import com.pipai.wf.item.weapon.Bow;
import com.pipai.wf.item.weapon.InnateCasting;
import com.pipai.wf.item.weapon.Pistol;
import com.pipai.wf.item.weapon.Weapon;
import com.pipai.wf.misc.BasicStats;
import com.pipai.wf.unit.ability.AbilityList;
import com.pipai.wf.unit.ability.SuppressionAbility;
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
		Assert.assertEquals("scenario/main.txt", save.getVariable("scenario").get());
	}

	@Test
	public void saveVariableTest() {
		Save save = new Save();
		final String SCENARIO = "scenario";
		Assert.assertFalse(save.getVariable(SCENARIO).isPresent());
		final String SCENARIO_VALUE = "test_scenario.txt";
		save.setVariable(SCENARIO, SCENARIO_VALUE);
		Assert.assertEquals(SCENARIO_VALUE, save.getVariable(SCENARIO).get());
	}

	@Test
	public void saveLoadTest() throws URISyntaxException, CorruptedSaveException {
		Save save = new Save();
		SaveLoader loader = new SaveLoader(save);
		URL url = this.getClass().getResource("/com/pipai/wf/save/save1.txt");
		File file = new File(url.getFile());
		FileHandle handle = new FileHandle(file);
		loader.load(handle);
		List<UnitSchema> party = save.getParty();
		Assert.assertEquals(1, party.size());
		UnitSchema schema = party.get(0);
		Assert.assertEquals("Tidus", schema.getName());
		Assert.assertEquals(6, schema.getHP());
		Assert.assertEquals(7, schema.getMaxHP());
		Assert.assertEquals(4, schema.getMP());
		Assert.assertEquals(5, schema.getMaxMP());
		Assert.assertEquals(1, schema.getAP());
		Assert.assertEquals(2, schema.getMaxAP());
		Assert.assertEquals(65, schema.getAim());
		Assert.assertEquals(12, schema.getMobility());
		Assert.assertEquals(0, schema.getDefense());
		Assert.assertEquals(1, schema.getAbilities().size());
		Assert.assertTrue(schema.getAbilities().getAbility(SuppressionAbility.class) instanceof SuppressionAbility);
		Assert.assertTrue(schema.getInventory().getItem(1) instanceof LeatherArmor);
		Assert.assertEquals(3, ((LeatherArmor) schema.getInventory().getItem(1)).getHP());
		Assert.assertTrue(schema.getInventory().getItem(2) instanceof Bow);
		Assert.assertEquals(2, ((Bow) schema.getInventory().getItem(2)).getCurrentAmmo());
		Assert.assertTrue(schema.getInventory().getItem(3) instanceof InnateCasting);
		Assert.assertEquals(1, schema.getLevel());
		Assert.assertEquals(10, schema.getExp());
		Assert.assertEquals("hello.txt", save.getVariable("scenario").get());
		Assert.assertEquals("opening", save.getVariable("label").get());
		Assert.assertEquals("2016", save.getVariable("year").get());
	}

	@Test
	public void saveWriteVariablesTest() throws URISyntaxException, CorruptedSaveException, IOException {
		Save save = new Save();
		final String SCENARIO = "scenario";
		final String SCENARIO_VALUE = "test.txt";
		final String LABEL = "label";
		final String LABEL_VALUE = "opening";
		save.setVariable(SCENARIO, SCENARIO_VALUE);
		save.setVariable(LABEL, LABEL_VALUE);
		MANAGER.save(save, SAVE_SLOT);
		Save load = MANAGER.load(SAVE_SLOT);
		Assert.assertEquals(SCENARIO_VALUE, load.getVariable(SCENARIO).get());
		Assert.assertEquals(LABEL_VALUE, load.getVariable(LABEL).get());
	}

}
