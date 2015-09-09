package com.pipai.wf.battle.vision;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.guiobject.battle.AgentGuiObject;

public class FogOfWar {

	private BattleMap map;
	// private HashMap<GridPosition, Boolean>
	private List<AgentGuiObject> agents;

	public FogOfWar(BattleMap map, List<AgentGuiObject> agents) {
		this.map = map;
		this.agents = agents;
	}

	public Set<GridPosition> visibleTiles() {
		Set<GridPosition> l = new HashSet<GridPosition>();
		return l;
	}

}
