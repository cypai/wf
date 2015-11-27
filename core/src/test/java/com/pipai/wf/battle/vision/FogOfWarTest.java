package com.pipai.wf.battle.vision;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.guiobject.battle.AgentGuiObject;

/**
 * For now, ignore all fog of war tests until FogOfWar's Pixmap is dealt with
 */
public class FogOfWarTest {

	private AgentGuiObject generateMockAgent(Agent a) {
		AgentGuiObject mockAgent = Mockito.mock(AgentGuiObject.class);
		Mockito.when(mockAgent.getDisplayPosition()).thenReturn(a.getPosition());
		return mockAgent;
	}

	@Ignore
	@Test
	public void testTinyVisionRadius() {
		final int VISUAL_RANGE = 1;
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		Mockito.when(mockConfig.sightRange()).thenReturn(VISUAL_RANGE);
		Mockito.when(mockConfig.sightRangeAdjusted()).thenCallRealMethod();
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(2, 2);
		AgentStateFactory factory = new AgentStateFactory();
		map.addAgent(factory.battleAgentFromStats(Team.PLAYER, playerPos, 1, 1, 1, 1, 1, 0));
		AgentGuiObject playerGuiObj = generateMockAgent(map.getAgentAtPos(playerPos));
		ArrayList<AgentGuiObject> playerList = new ArrayList<>();
		playerList.add(playerGuiObj);
		FogOfWar fog = new FogOfWar(map, playerList);
		fog.fullScan();
		for (GridPosition p : fog.visibleTiles()) {
			// Assert all viewable tiles have some part within viewing range
			Assert.assertTrue(playerPos.distance(p) < mockConfig.sightRangeAdjusted());
		}
		Assert.assertEquals(4, fog.visibleTiles().size());
	}

	@Ignore
	@Test
	public void testSmallVisionRadius() {
		final int VISUAL_RANGE = 2;
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		Mockito.when(mockConfig.sightRange()).thenReturn(VISUAL_RANGE);
		Mockito.when(mockConfig.sightRangeAdjusted()).thenCallRealMethod();
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(2, 2);
		AgentStateFactory factory = new AgentStateFactory();
		map.addAgent(factory.battleAgentFromStats(Team.PLAYER, playerPos, 1, 1, 1, 1, 1, 0));
		AgentGuiObject playerGuiObj = generateMockAgent(map.getAgentAtPos(playerPos));
		ArrayList<AgentGuiObject> playerList = new ArrayList<>();
		playerList.add(playerGuiObj);
		FogOfWar fog = new FogOfWar(map, playerList);
		fog.fullScan();
		for (GridPosition p : fog.visibleTiles()) {
			// Assert all viewable tiles have some part within viewing range
			Assert.assertTrue(playerPos.distance(p) < mockConfig.sightRangeAdjusted());
		}
		Assert.assertEquals(20, fog.visibleTiles().size());
	}

}
