package com.pipai.wf.save;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.libgdx.test.GdxMockedTest;
import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;

public class SaveTest extends GdxMockedTest {

	private static int SAVE_SLOT = 0;
	private static SaveManager MANAGER = new SaveManager("");

	@After
	public void tearDown() {
		// MANAGER.delete(SAVE_SLOT);
	}

	@Test
	public void newGameTest() throws IOException {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		NewGameSaveGenerator generator = new NewGameSaveGenerator(mockConfig);
		Save save = generator.generateNewSave();
		MANAGER.save(save, SAVE_SLOT);
		Save load = MANAGER.load(SAVE_SLOT);
		ArrayList<AgentState> party = load.getParty();
		Assert.assertEquals(6, party.size());
	}

	@Test
	public void test() {
		AgentStateFactory factory = new AgentStateFactory(Mockito.mock(BattleConfiguration.class));
		AgentState as = factory.statsOnlyState(5, 4, 2, 10, 60, 0);
		Save save = new Save();
		ArrayList<AgentState> party = new ArrayList<>();
		party.add(as);
		save.setParty(party);
	}

}
