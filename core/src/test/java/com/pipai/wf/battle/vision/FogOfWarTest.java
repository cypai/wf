package com.pipai.wf.battle.vision;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Test;

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
		AgentGuiObject mockAgent = mock(AgentGuiObject.class);
		when(mockAgent.getDisplayPosition()).thenReturn(a.getPosition());
		return mockAgent;
	}

	@Ignore
	@Test
	public void testTinyVisionRadius() {
		final int VISUAL_RANGE = 1;
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		when(mockConfig.sightRange()).thenReturn(VISUAL_RANGE);
		when(mockConfig.sightRangeAdjusted()).thenCallRealMethod();
		BattleMap map = new BattleMap(5, 5, mock(BattleConfiguration.class));
		GridPosition playerPos = new GridPosition(2, 2);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		map.addAgent(factory.battleAgentFromStats(Team.PLAYER, playerPos, factory.statsOnlyState(1, 1, 1, 1, 1, 0)));
		AgentGuiObject playerGuiObj = generateMockAgent(map.getAgentAtPos(playerPos));
		ArrayList<AgentGuiObject> playerList = new ArrayList<>();
		playerList.add(playerGuiObj);
		FogOfWar fog = new FogOfWar(map, playerList);
		fog.fullScan();
		for (GridPosition p : fog.visibleTiles()) {
			// Assert all viewable tiles have some part within viewing range
			assertTrue(playerPos.distance(p) < mockConfig.sightRangeAdjusted());
		}
		assertTrue(fog.visibleTiles().size() == 4);
	}

	@Ignore
	@Test
	public void testSmallVisionRadius() {
		final int VISUAL_RANGE = 2;
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		when(mockConfig.sightRange()).thenReturn(VISUAL_RANGE);
		when(mockConfig.sightRangeAdjusted()).thenCallRealMethod();
		BattleMap map = new BattleMap(5, 5, mock(BattleConfiguration.class));
		GridPosition playerPos = new GridPosition(2, 2);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		map.addAgent(factory.battleAgentFromStats(Team.PLAYER, playerPos, factory.statsOnlyState(1, 1, 1, 1, 1, 0)));
		AgentGuiObject playerGuiObj = generateMockAgent(map.getAgentAtPos(playerPos));
		ArrayList<AgentGuiObject> playerList = new ArrayList<>();
		playerList.add(playerGuiObj);
		FogOfWar fog = new FogOfWar(map, playerList);
		fog.fullScan();
		for (GridPosition p : fog.visibleTiles()) {
			// Assert all viewable tiles have some part within viewing range
			assertTrue(playerPos.distance(p) < mockConfig.sightRangeAdjusted());
		}
		assertTrue(fog.visibleTiles().size() == 20);
	}

}
