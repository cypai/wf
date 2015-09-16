package com.pipai.wf.test.battle;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Test;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.vision.FogOfWar;
import com.pipai.wf.config.BattleProperties;
import com.pipai.wf.config.WFConfig;
import com.pipai.wf.guiobject.battle.AgentGuiObject;
import com.pipai.wf.test.WfConfiguredTest;

/**
 * For now, ignore all fog of war tests until FogOfWar's Pixmap is dealt with
 */
public class FogOfWarTest extends WfConfiguredTest {

	private AgentGuiObject generateMockAgent(Agent a) {
		AgentGuiObject mockAgent = mock(AgentGuiObject.class);
		when(mockAgent.getDisplayPosition()).thenReturn(a.getPosition());
		return mockAgent;
	}

	@Ignore
	@Test
	public void testTinyVisionRadius() {
		final int VISUAL_RANGE = 1;
		BattleProperties props = mock(BattleProperties.class);
		when(props.sightRange()).thenReturn(VISUAL_RANGE);
		when(props.sightRangeAdjusted()).thenCallRealMethod();
		WFConfig.setBattleProps(props);
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(2, 2);
		map.addAgent(AgentStateFactory.battleAgentFromStats(Team.PLAYER, playerPos, AgentStateFactory.statsOnlyState(1, 1, 1, 1, 1, 0)));
		AgentGuiObject playerGuiObj = generateMockAgent(map.getAgentAtPos(playerPos));
		ArrayList<AgentGuiObject> playerList = new ArrayList<AgentGuiObject>();
		playerList.add(playerGuiObj);
		FogOfWar fog = new FogOfWar(map, playerList);
		fog.fullScan();
		for (GridPosition p : fog.visibleTiles()) {
			// Assert all viewable tiles have some part within viewing range
			assertTrue(playerPos.distance(p) < WFConfig.battleProps().sightRangeAdjusted());
		}
		assertTrue(fog.visibleTiles().size() == 4);
	}

	@Ignore
	@Test
	public void testSmallVisionRadius() {
		final int VISUAL_RANGE = 2;
		BattleProperties props = mock(BattleProperties.class);
		when(props.sightRange()).thenReturn(VISUAL_RANGE);
		when(props.sightRangeAdjusted()).thenCallRealMethod();
		WFConfig.setBattleProps(props);
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(2, 2);
		map.addAgent(AgentStateFactory.battleAgentFromStats(Team.PLAYER, playerPos, AgentStateFactory.statsOnlyState(1, 1, 1, 1, 1, 0)));
		AgentGuiObject playerGuiObj = generateMockAgent(map.getAgentAtPos(playerPos));
		ArrayList<AgentGuiObject> playerList = new ArrayList<AgentGuiObject>();
		playerList.add(playerGuiObj);
		FogOfWar fog = new FogOfWar(map, playerList);
		fog.fullScan();
		for (GridPosition p : fog.visibleTiles()) {
			// Assert all viewable tiles have some part within viewing range
			assertTrue(playerPos.distance(p) < WFConfig.battleProps().sightRangeAdjusted());
		}
		assertTrue(fog.visibleTiles().size() == 20);
	}

}
